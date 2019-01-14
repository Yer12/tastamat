package kz.tastamat.app.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import kz.zx.api.app.DbHandler;

public class SecurityHandler extends DbHandler {

    public SecurityHandler(Vertx vertx) {
        super(vertx);
    }

	public void handle(String token, Handler<AsyncResult<JsonObject>> handler) {
//    	try {
//			JsonObject obj = new JsonObject();
//			obj.put("token", token);
//
//			eventBus.send(FirebaseVerticle.ADDRESS, obj, action(FirebaseVerticle.Action.GET_USER), (AsyncResult<Message<JsonObject>> res) -> {
//				if(res.succeeded()){
//					Message<JsonObject> message = res.result();
//					handler.handle(Future.succeededFuture(message.body()));
//				} else {
//					handler.handle(Future.failedFuture(res.cause()));
//				}
//			});
//		} catch (Exception e) {
//			handler.handle(Future.failedFuture(ApiException.build(Errors.ACCESS, "unauthorized")));
//		}
	}
}
