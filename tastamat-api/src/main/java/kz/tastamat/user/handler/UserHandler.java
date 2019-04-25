package kz.tastamat.user.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.auth.dto.ConfirmDto;
import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.sms.dto.SmsDto;
import kz.tastamat.sms.handler.SmsHandler;
import kz.tastamat.user.bean.UserBean;
import kz.tastamat.auth.dto.LoginDto;
import kz.tastamat.user.dto.UserInfoDto;
import kz.zx.api.app.DbHandler;

/**
 * Created by baur on 10/24/17.
 */
public class UserHandler extends DbHandler {

	private Logger log = LoggerFactory.getLogger(UserHandler.class);

	private final SmsHandler smsHandler;

	public UserHandler(Vertx vertx) {
		super(vertx);
		this.smsHandler = new SmsHandler(vertx);
	}

	public void getFullInfo(Long id, Handler<AsyncResult<UserInfoDto>> handler) {
		blocking(dsl -> new UserBean(dsl).getFullInfo(id), handler);
	}

	public void handleExists(String phone, Handler<AsyncResult<Boolean>> handler) {
		blocking(dsl -> new UserBean(dsl).exists(phone), handler);
	}

	public void initialize(UserInfoDto dto, Handler<AsyncResult<UserInfoDto>> handler) {
		UserDto user = new UserDto();
		user.phone = dto.phone;

		blocking(dsl -> {
			return UserBean.build(dsl, this.config).initialize(user);
		}, br -> {
			if (br.succeeded()) {
				UserInfoDto u = br.result();
				handler.handle(Future.succeededFuture(u));
			} else {
				handler.handle(Future.failedFuture(br.cause()));
			}
		});
	}

	public void sms(String phone, Handler<AsyncResult<UserInfoDto>> handler) {
		blocking(dsl -> {
			return UserBean.build(dsl).sms(phone);
		}, br -> {
			if (br.succeeded()) {
				UserDto user = br.result();
				SmsDto smsDto = new SmsDto();
				smsDto.phone = user.phone;
				smsDto.message = "Ваш тастамат код: " + user.smsCode;

				smsHandler.sendSms(smsDto, ar -> {
					if (ar.failed()) {
						log.error(ar.cause());
					}
				});

				handler.handle(Future.succeededFuture(UserInfoDto.build(user)));
			} else {
				handler.handle(Future.failedFuture(br.cause()));
			}
		});
	}

	public void confirm(ConfirmDto confirmDto, Handler<AsyncResult<UserInfoDto>> handler) {
		blocking(dsl -> UserInfoDto.build(UserBean.build(dsl).confirm(confirmDto)), handler);
	}

	public void password(ConfirmDto dto, Handler<AsyncResult<UserInfoDto>> handler) {
		blocking(dsl -> UserBean.build(dsl).password(dto), handler);
	}

	public void login(LoginDto dto, Handler<AsyncResult<UserInfoDto>> handler) {
		log.info("login {}, password {}", dto.phone, dto.password);
		blocking(dsl -> UserBean.build(dsl).login(dto.phone, dto.password), handler);
	}
}
