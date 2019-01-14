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

	public void handleExists(String phone, Handler<AsyncResult<Boolean>> handler) {
		blocking(dsl -> new UserBean(dsl).exists(phone), handler);
	}

	public void handleSms(String phone, Handler<AsyncResult<UserDto>> handler) {
		blocking(dsl -> {
			return UserBean.build(dsl, this.config).sms(phone);
		}, br -> {
			if(br.succeeded()){
				UserDto user = br.result();
				SmsDto smsDto = new SmsDto();
				smsDto.phone = user.phone;
				smsDto.message = "Ваш тастамат код: "+user.smsCode;

				smsHandler.sendSms(smsDto, ar -> {
					if (ar.failed()) {
						log.error(ar.cause());
					}
				});

				handler.handle(Future.succeededFuture(user));
			} else {
				handler.handle(Future.failedFuture(br.cause()));
			}
		});
	}

	public void handleConfirm(ConfirmDto confirmDto, Handler<AsyncResult<UserDto>> handler) {
		blocking(dsl -> new UserBean(dsl).confirm(confirmDto), handler);
	}

	public void handlePassword(ConfirmDto dto, Handler<AsyncResult<UserDto>> handler) {
		blocking(dsl -> new UserBean(dsl).password(dto), handler);
	}

	public void handleLogin(LoginDto dto, Handler<AsyncResult<UserDto>> handler) {
		log.info("login {}, password {}", dto.phone, dto.password);
		blocking(dsl -> new UserBean(dsl).login(dto.phone, dto.password), handler);
	}
}
