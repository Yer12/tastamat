package kz.tastamat.sms.verticle;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.enums.ConfigKey;
import kz.tastamat.sms.dto.SmsDto;
import kz.tastamat.utils.XmlUtils;
import kz.zx.api.app.BaseVerticle;
import kz.zx.exceptions.ApiException;
import kz.zx.json.Mapper;
import org.tempuri.*;

public class SmsVerticle extends BaseVerticle {

	private Logger log = LoggerFactory.getLogger(SmsVerticle.class);

	public enum Action {
		SEND_SMS
	}

	public static final String ADDRESS = "SmsVerticle";
	private HttpClient client;
	protected JsonObject sms;

	@Override
	public void start() throws Exception {
		super.start();
		this.sms = config().getJsonObject(ConfigKey.SMS.key());

		HttpClientOptions options = new HttpClientOptions().setSsl(true).setTrustAll(true);
		this.client = vertx.createHttpClient(options);

		consumer(ADDRESS).on(Action.SEND_SMS, this::handleSendSms).build(Action.class);
	}

	private void handleSendSms(Message<JsonObject> mes) {

		SmsDto smsDto = Mapper.map(SmsDto.class, mes.body());

		String url = this.sms.getString(ConfigKey.URL.key());

		SendSMSServiceSendMessage message = new SendSMSServiceSendMessage();

		message.setLogin(this.sms.getString(ConfigKey.LOGIN.key()));
		message.setPassword(this.sms.getString(ConfigKey.PASSWORD.key()));

		SMSM smsm = new SMSM();
		smsm.setMsg(smsDto.message);
		smsm.setRecepient(smsDto.phone);
		smsm.setSenderid(this.sms.getString(ConfigKey.SENDER_ID.key()));
		smsm.setMsgtype(this.sms.getInteger(ConfigKey.MSG_TYPE.key()));
		smsm.setUserMsgID(this.sms.getString(ConfigKey.USER_MSG_ID.key()));
		smsm.setScheduled(this.sms.getString(ConfigKey.SCHEDULED.key()));
		smsm.setPrioritet(this.sms.getInteger(ConfigKey.PRIORITY.key()));
		message.setSms(smsm);

		XmlUtils.toSoapMessageString(vertx, message, request -> {
			log.info("phone {} : request {}", smsDto.phone, request.result());
			Handler<Throwable> exHandler = (Throwable ex) -> handle(mes, ex);
			client.postAbs(url, r -> {
				r.bodyHandler(body -> {
					log.info("phone {} : responce {}", smsDto.phone, body.toString());
					XmlUtils.fromSoapMessage(vertx, body.toString(), r.headers(), SendSMSServiceSendMessageResponse.class, res -> {
						if (res.succeeded()) {
							SendSMSServiceSendMessageResponse response = res.result();
							if(response.getResult().getStatusCode() == 0){
								handle(mes, new JsonObject());
							} else {
								handle(mes, ApiException.unexpected(response.getResult().getStatus()));
							}
						} else {
							handle(mes, res.cause());
						}
					});
				});
			}).exceptionHandler(exHandler)
					.putHeader(HttpHeaders.CONTENT_TYPE, "text/xml; charset=UTF-8")
					.setChunked(true)
					.write(request.result())
					.end();
		});
	}

}
