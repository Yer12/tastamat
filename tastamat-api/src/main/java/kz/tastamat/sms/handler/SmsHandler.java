package kz.tastamat.sms.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.sms.dto.SmsDto;
import kz.tastamat.sms.verticle.SmsVerticle;
import kz.zx.api.app.DbHandler;
import kz.zx.api.enums.Key;

public class SmsHandler extends DbHandler {

	private static final DeliveryOptions SEND_SMS_ACTION = new DeliveryOptions().addHeader(Key.action, SmsVerticle.Action.SEND_SMS.name());

	private Logger log = LoggerFactory.getLogger(SmsHandler.class);

	public SmsHandler(Vertx vertx) {
		super(vertx);
	}

	public void sendSms(SmsDto smsDto, Handler<AsyncResult<JsonObject>> handler) {
		vertx.eventBus().send(SmsVerticle.ADDRESS, JsonObject.mapFrom(smsDto), SEND_SMS_ACTION, (AsyncResult<Message<JsonObject>> ar) -> {
			if (ar.succeeded()) {
				log.info("handler result {}", ar.result().body());
				handler.handle(Future.succeededFuture(ar.result().body()));
			} else {
				handler.handle(Future.failedFuture(ar.cause()));
			}
		});
	}
}
