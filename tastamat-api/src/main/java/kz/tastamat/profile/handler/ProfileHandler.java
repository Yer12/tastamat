package kz.tastamat.profile.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.auth.dto.ConfirmDto;
import kz.tastamat.db.model.dto.ProfileDto;
import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.profile.bean.ProfileBean;
import kz.tastamat.profile.dto.ProfileInfoDto;
import kz.zx.api.app.DbHandler;

/**
 * Created by baur on 10/24/17.
 */
public class ProfileHandler extends DbHandler {

	private Logger log = LoggerFactory.getLogger(ProfileHandler.class);

	public ProfileHandler(Vertx vertx) {
		super(vertx);
	}

	public void getProfile(Long id, Handler<AsyncResult<ProfileDto>> handler) {
		blocking(dsl -> new ProfileBean(dsl).getFullInfo(id), handler);
	}

	public void getUserProfile(Long id, Handler<AsyncResult<ProfileInfoDto>> handler) {
		blocking(dsl -> new ProfileBean(dsl).getFullInfoByUser(id), handler);
	}

	public void template(ProfileInfoDto dto, Handler<AsyncResult<ProfileDto>> handler) {
		blocking(dsl -> new ProfileBean(dsl).template(dto), handler);
	}
}
