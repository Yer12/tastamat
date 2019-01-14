package kz.zx.api.app;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import kz.zx.api.enums.Key;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by deedarb on 9/28/17.
 */
public class SoapService {

	private Vertx vertx;

	@Resource
	private WebServiceContext ctx;

	public final static String COURT_MESSAGE_ID = "courtMessageId";

	public SoapService(Vertx vertx) {
		this.vertx = vertx;
	}

	public void eventBus(String address, Enum action, Object data, Consumer<AsyncResult<Message<JsonObject>>> handler){
		DeliveryOptions opts = new DeliveryOptions();
		opts.addHeader(Key.action, action.name());

		MessageContext mc = ctx.getMessageContext();
		Map<String, List<String>> headers = (Map<String, List<String>>) mc.get(MessageContext.HTTP_REQUEST_HEADERS);
		if (headers.get(COURT_MESSAGE_ID) != null && !headers.get(COURT_MESSAGE_ID).isEmpty()) {
			String messageId = headers.get(COURT_MESSAGE_ID).get(0);
			opts.addHeader(Key.messageId, messageId);
		}

		vertx.eventBus().send(address, JsonObject.mapFrom(data), opts, (AsyncResult<Message<JsonObject>> ar) -> handler.accept(ar));
	}

}
