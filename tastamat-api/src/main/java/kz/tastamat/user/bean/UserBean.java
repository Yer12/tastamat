package kz.tastamat.user.bean;

import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.auth.dto.ConfirmDto;
import kz.tastamat.dao.ProfileDao;
import kz.tastamat.dao.UserDao;
import kz.tastamat.dao.impl.ProfileDaoImpl;
import kz.tastamat.dao.impl.UserDaoImpl;
import kz.tastamat.db.model.dto.ProfileDto;
import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.enums.ConfigKey;
import kz.tastamat.profile.dto.ProfileInfoDto;
import kz.tastamat.user.dto.UserInfoDto;
import kz.tastamat.utils.JsonUtils;
import kz.zx.exceptions.ApiException;
import kz.zx.utils.Holder;
import kz.zx.utils.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jooq.DSLContext;

import java.util.Optional;

/**
 * Created by baur on 10/24/17.
 */
public class UserBean {

	private final Logger log = LoggerFactory.getLogger(UserBean.class);

	private DSLContext ctx;

	private JsonObject config;

	public UserBean(DSLContext ctx) {
		this.ctx = ctx;
	}

	public UserBean(DSLContext ctx, JsonObject config) {
		this.ctx = ctx;
		this.config = config;
	}

	public static UserBean build(DSLContext ctx) {
		return new UserBean(ctx);
	}

	public static UserBean build(DSLContext ctx, JsonObject config) {
		return new UserBean(ctx, config);
	}

	public UserInfoDto getFullInfo(Long id) {
		return this.ctx.transactionResult(tr -> {
			DSLContext dsl = tr.dsl();
			JsonObject userError = JsonUtils.getDictionary("not.found.user", "", "Пользователь не найден", "", "");
			UserDto userDto = getUserDao(dsl).findById(id).orElseThrow(() -> ApiException.notFound(userError.toString()));

			UserInfoDto userInfo = UserInfoDto.build(userDto);

			getProfileDao(dsl).findByUser(userDto.id).ifPresent(p -> {
				userInfo.profile = ProfileInfoDto.build(p);
			});

			return userInfo;
		});
	}

	public Boolean exists(String phone) {
		return getUserDao(this.ctx).findEnabledByPhone(phone).isPresent();
	}

	public UserInfoDto initialize(UserDto dto) {

		UserDao userDao = getUserDao(this.ctx);
		Holder<UserInfoDto> holder = new Holder<>();

		if (StringUtils.isNotEmpty(dto.phone)) {
			userDao.findByPhone(dto.phone).ifPresent(user -> {
				JsonObject userError = JsonUtils.getDictionary("phone.exists.user", "", "Пользователь с таким телефоном уже существует", "", "");
				if (user.enabled) {
					throw ApiException.business(userError.encode());
				} else {
					holder.set(UserInfoDto.build(user));
				}
			});
		}

		JsonObject profile = this.config.getJsonObject(ConfigKey.PROFILE.key());
		Long wallet = profile.getLong(ConfigKey.WALLET.key());
		String template = profile.getString(ConfigKey.TEMPLATE.key());

		if (holder.get() != null) {
			return holder.get();
		} else {
			return this.ctx.transactionResult(tr -> {
				DSLContext dsl = tr.dsl();
				UserInfoDto user = UserInfoDto.build(getUserDao(dsl).initialize(dto));
				getProfileDao(dsl).create(user.id, wallet, template);
				return user;
			});
		}
	}

	public UserDto sms(String phone) {
		JsonObject userError = JsonUtils.getDictionary("phone.not.found.user", "", "Пользователь не найден", "", "");
		UserDto user = getUserDao(this.ctx).findByPhone(phone).orElseThrow(() -> ApiException.notFound(userError.encode()));
		String smsCode = RandomStringUtils.randomNumeric(4);
		return getUserDao(this.ctx).sms(user.id, smsCode);
	}

	public UserDto confirm(ConfirmDto confirmDto) {
		JsonObject error = JsonUtils.getDictionary("not.found.auth.user", "", "Пользователь не найден", "", "");
		UserDto userDto = getUserDao(this.ctx).findById(confirmDto.id).orElseThrow(() -> ApiException.notFound(error.toString()));

		JsonObject phoneError = JsonUtils.getDictionary("not.found.auth.invalid.phone", "", "Телефон не верен", "", "");
		if (!confirmDto.phone.equals(userDto.phone)) {
			throw ApiException.business(phoneError.toString());
		}

		JsonObject codeError = JsonUtils.getDictionary("not.found.auth.invalid.code", "", "Код не верен", "", "");
		if (!confirmDto.code.equals(userDto.smsCode)) {
			throw ApiException.business(codeError.toString());
		}

		return userDto;
	}

	public UserInfoDto password(ConfirmDto dto) {
		JsonObject error = JsonUtils.getDictionary("not.found.auth.user", "", "Пользователь не найден", "", "");
		UserDto userDto = getUserDao(this.ctx).findById(dto.id).orElseThrow(() -> ApiException.notFound(error.toString()));

		JsonObject phoneError = JsonUtils.getDictionary("not.found.auth.invalid.phone", "", "Телефон не верен", "", "");
		if (!dto.phone.equals(userDto.phone)) {
			throw ApiException.business(phoneError.toString());
		}

		JsonObject codeError = JsonUtils.getDictionary("not.found.auth.invalid.code", "", "Код не верен", "", "");
		if (!dto.code.equals(userDto.smsCode)) {
			throw ApiException.business(codeError.toString());
		}

		String pass = DigestUtils.md5Hex(dto.password);

		return this.ctx.transactionResult(tr -> {
			DSLContext dsl = tr.dsl();

			getUserDao(dsl).enable(userDto.id);
			UserDto u = getUserDao(dsl).password(userDto.id, pass);
			UserInfoDto user = UserInfoDto.build(u);
			getProfileDao(dsl).findByUser(u.id).ifPresent(profileDto -> user.profile = ProfileInfoDto.build(profileDto));

			return user;
		});
	}

	public UserInfoDto login(String phone, String password) {
		return this.ctx.transactionResult(tr -> {
			DSLContext dsl = tr.dsl();

			JsonObject error = JsonUtils.getDictionary("not.found.auth.user", "", "Пользователь не найден", "", "");
			UserDto userDto = getUserDao(dsl).findByPhonePassword(phone, password).orElseThrow(() -> ApiException.notFound(error.toString()));
			UserInfoDto user = UserInfoDto.build(userDto);

			getProfileDao(dsl).findByUser(userDto.id).ifPresent(dto -> user.profile = ProfileInfoDto.build(dto));
			return user;
		});
	}

//	public boolean hasRole(String email, Role role) {
//		return getUserDao(this.ctx).hasRole(email, role);
//	}

	private UserDao getUserDao(DSLContext dsl) {
		return new UserDaoImpl(dsl);
	}

	private ProfileDao getProfileDao(DSLContext dsl) {
		return new ProfileDaoImpl(dsl);
	}
}
