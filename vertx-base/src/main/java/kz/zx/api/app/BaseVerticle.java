package kz.zx.api.app;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import kz.zx.api.enums.Errors;
import kz.zx.api.enums.Key;
import kz.zx.exceptions.ApiException;
import kz.zx.utils.PaginatedList;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by deedarb on 8/2/17.
 */
public class BaseVerticle extends AbstractVerticle {

	private Map<String, Consumer<Message<JsonObject>>> actions = new HashMap<>();

	private String address;

	private static Logger log = LoggerFactory.getLogger(BaseVerticle.class);

	public BaseVerticle consumer(String address) {
		this.address = address;
		return this;
	}

	public BaseVerticle on(Enum action, Consumer<Message<JsonObject>> consumer) {
		actions.put(action.name(), consumer);
		return this;
	}

	public void build(Class<? extends Enum> enumClass) throws Exception {
		vertx.eventBus().consumer(address, (Message<JsonObject> msg) -> {
			Enum action = Enum.valueOf(enumClass, msg.headers().get(Key.action));

			try {
				actions.get(action.name()).accept(msg);
			} catch (ApiException ex) {
				msg.fail(ex.error.code(), ex.getMessage());
			} catch (Throwable ex) {
				log.error("error", ex);
				msg.fail(Errors.UNEXPECTED.code(), ApiException.build(Errors.UNEXPECTED, ex).getMessage());
			}
		});
	}

	public void handle(Message msg, PaginatedList list) {
		JsonObject res = new JsonObject();
		res.put("count", list.getCount());
		res.put("list", list);
		msg.reply(res);
	}

	public void handle(Message msg, JsonObject object) {
		msg.reply(object);
	}

	public void handle(Message msg, Throwable exception) {
		if (exception instanceof ApiException) {
			ApiException ex = (ApiException) exception;
			log.error("api exception:", exception);
			msg.fail(ex.error.code(), ex.getMessage());
		} else {
			log.error("unexpected:", exception);
			msg.fail(Errors.UNEXPECTED.code(), "unexpected");
		}
	}
}
