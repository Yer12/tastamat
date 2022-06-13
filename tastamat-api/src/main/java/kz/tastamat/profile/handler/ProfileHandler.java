package kz.tastamat.profile.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.db.model.dto.ProfileDto;
import kz.tastamat.profile.bean.ProfileBean;
import kz.tastamat.profile.dto.BalanceWithdrawDto;
import kz.tastamat.profile.dto.ProfileInfoDto;
import kz.zx.api.app.DbHandler;

/**
 * Created by baur on 10/24/17.
 */
public class ProfileHandler extends DbHandler {

	public ProfileHandler(Vertx vertx) {
		super(vertx);
	}

	public void getProfile(Long id, Handler<AsyncResult<ProfileDto>> handler) {
		blocking(dsl -> new ProfileBean(dsl).getFullInfo(id), handler);
	}

	public void getUserProfile(Long id, Handler<AsyncResult<ProfileInfoDto>> handler) {
		blocking(dsl -> new ProfileBean(dsl).getFullInfoByUser(id), handler);
	}

	public void getUserProfileWallet(String phone, Handler<AsyncResult<ProfileInfoDto>> handler) {
		blocking(dsl -> new ProfileBean(dsl).getFullInfoByUserPhone(phone), handler);
	}

	public void withdrawBalance(BalanceWithdrawDto dto, Handler<AsyncResult<ProfileInfoDto>> handler) {
		blocking(dsl -> ProfileBean.build(dsl).withdrawBalance(dto), handler);
	}

	public void template(ProfileInfoDto dto, Handler<AsyncResult<ProfileDto>> handler) {
		blocking(dsl -> new ProfileBean(dsl).template(dto), handler);
	}
}
