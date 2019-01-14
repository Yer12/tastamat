package kz.tastamat.profile.bean;

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
import kz.tastamat.profile.dto.ProfileInfoDto;
import kz.tastamat.utils.JsonUtils;
import kz.zx.exceptions.ApiException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jooq.DSLContext;

import java.util.Optional;

/**
 * Created by baur on 10/24/17.
 */
public class ProfileBean {

	private final Logger log = LoggerFactory.getLogger(ProfileBean.class);

	private DSLContext ctx;

	public ProfileBean(DSLContext ctx) {
		this.ctx = ctx;
	}

	public static ProfileBean build(DSLContext ctx) {
		return new ProfileBean(ctx);
	}

	public ProfileDto getFullInfo(Long id) {
		JsonObject profileError = JsonUtils.getDictionary("not.found.profile", "", "Профиль не найден", "", "");
		ProfileDto profileDto = getProfileDao(this.ctx).findById(id).orElseThrow(() -> ApiException.notFound(profileError.toString()));
		return profileDto;
	}

	public ProfileDto getFullInfoByUser(Long userId) {
		JsonObject profileError = JsonUtils.getDictionary("not.found.profile", "", "Профиль не найден", "", "");
		ProfileDto profileDto = getProfileDao(this.ctx).findByUser(userId).orElseThrow(() -> ApiException.notFound(profileError.toString()));
		return profileDto;
	}

	public ProfileDto template(ProfileInfoDto profile) {
		getProfileDao(this.ctx).template(profile.id, profile.template);
		return getFullInfo(profile.id);
	}

	public int wallet(Long id, Long wallet) {
		return getProfileDao(this.ctx).wallet(id, wallet);
	}


	private ProfileDao getProfileDao(DSLContext dsl) {
		return new ProfileDaoImpl(dsl);
	}
}
