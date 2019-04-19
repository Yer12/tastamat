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

	public UserDto sms(String phone){
		Optional<UserDto> result = getUserDao(this.ctx).findByPhone(phone);

		if(result.isPresent()) {
			UserDto user = result.get();

			JsonObject userError = JsonUtils.getDictionary("not.found.auth.invalid.user", "", "Пользователь уже существует", "", "");

			if(user.enabled){
				throw ApiException.business(userError.toString());
			}

			String smsCode = RandomStringUtils.randomNumeric(4);
			UserDto userDto = getUserDao(this.ctx).clean(user.id, smsCode);
			userDto.phone = phone;
			return userDto;
		} else {
			String smsCode = RandomStringUtils.randomNumeric(4);

			JsonObject profile = this.config.getJsonObject(ConfigKey.PROFILE.key());
			Long wallet = profile.getLong(ConfigKey.WALLET.key());
			String template = profile.getString(ConfigKey.TEMPLATE.key());

			return ctx.transactionResult(tr -> {
				DSLContext dsl = tr.dsl();

				UserDto user = getUserDao(dsl).create(phone, smsCode);
				getProfileDao(dsl).create(user.id, wallet, template);
				return user;
			});
		}
	}

	public UserDto confirm(ConfirmDto confirmDto) {
		JsonObject error = JsonUtils.getDictionary("not.found.auth.user", "", "Пользователь не найден", "", "");
		UserDto userDto = getUserDao(this.ctx).findById(confirmDto.id).orElseThrow(() -> ApiException.notFound(error.toString()));

		JsonObject userError = JsonUtils.getDictionary("not.found.auth.invalid.user", "", "Пользователь уже существует", "", "");
		if(userDto.enabled){
			throw ApiException.business(userError.toString());
		}

		JsonObject phoneError = JsonUtils.getDictionary("not.found.auth.invalid.phone", "", "Телефон не верен", "", "");
		if(!confirmDto.phone.equals(userDto.phone)){
			throw ApiException.business(phoneError.toString());
		}

		JsonObject codeError = JsonUtils.getDictionary("not.found.auth.invalid.code", "", "Код не верен", "", "");
		if(!confirmDto.code.equals(userDto.smsCode)){
			throw ApiException.business(codeError.toString());
		}

		return getUserDao(this.ctx).confirm(userDto.id);
	}

	public UserDto password(ConfirmDto dto) {
		JsonObject error = JsonUtils.getDictionary("not.found.auth.user", "", "Пользователь не найден", "", "");
		UserDto userDto = getUserDao(this.ctx).findById(dto.id).orElseThrow(() -> ApiException.notFound(error.toString()));

		JsonObject userError = JsonUtils.getDictionary("not.found.auth.invalid.user", "", "Пользователь уже существует", "", "");
		if(userDto.enabled){
			throw ApiException.business(userError.toString());
		}

		JsonObject phoneError = JsonUtils.getDictionary("not.found.auth.invalid.phone", "", "Телефон не верен", "", "");
		if(!dto.phone.equals(userDto.phone)){
			throw ApiException.business(phoneError.toString());
		}

		String pass = DigestUtils.md5Hex(dto.password);

		return getUserDao(this.ctx).enable(userDto.id, pass);
	}

	public UserDto login(String phone, String password) {
		JsonObject error = JsonUtils.getDictionary("not.found.auth.user", "", "Пользователь не найден", "", "");
		UserDto userDto = getUserDao(this.ctx).findByPhonePassword(phone, password).orElseThrow(() -> ApiException.notFound(error.toString()));
		return userDto;
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
