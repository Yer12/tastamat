package kz.toolpar.message.client;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import kz.toolpar.message.client.dto.MessageInfoDto;
import kz.toolpar.message.client.impl.MessageClientImpl;

public interface MessageClient {

    static MessageClient create(Vertx vertx) {
        return new MessageClientImpl(vertx);
    }

    Future<JsonObject> sendGet(MessageInfoDto messageInfo);
    Future<JsonObject> sendPut(MessageInfoDto messageInfo);

    Future<JsonObject> sendHttpGet(MessageInfoDto messageInfoDto);
}
