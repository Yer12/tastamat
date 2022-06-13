package kz.toolpar.message.client.impl;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import kz.toolpar.message.client.MessageClient;
import kz.toolpar.message.client.dto.MessageInfoDto;
import kz.toolpar.message.client.dto.MessageInfoHeader;

public class MessageClientImpl implements MessageClient {

    private final Vertx vertx;
    private final WebClient webClient;
    private final HttpClient httpClient;

    public MessageClientImpl(Vertx vertx) {
        this.vertx = vertx;
        this.webClient = WebClient.create(vertx);

        HttpClientOptions options = new HttpClientOptions().setSsl(true).setTrustAll(true);
        this.httpClient = vertx.createHttpClient(options);
    }

    @Override
    public Future<JsonObject> sendGet(MessageInfoDto messageInfo) {
        Future<JsonObject> future = Future.future();
        HttpRequest<JsonObject> request = webClient.getAbs(messageInfo.getUrl())
                .as(BodyCodec.jsonObject());
        for (MessageInfoHeader header : messageInfo.getHeaders()) {
            request.putHeader(header.getKey(), header.getValue());
        }

        request.send(sr -> {
            if (sr.succeeded()) {
                future.complete(sr.result().body());
            } else {
                future.fail(sr.cause());
            }
        });

        return future;
    }

    @Override
    public Future<JsonObject> sendPut(MessageInfoDto messageInfo) {
        Future<JsonObject> future = Future.future();
        HttpRequest<JsonObject> request = webClient.putAbs(messageInfo.getUrl())
                .as(BodyCodec.jsonObject());

        for (MessageInfoHeader header : messageInfo.getHeaders()) {
            request.putHeader(header.getKey(), header.getValue());
        }

        request.sendJson(new JsonObject(messageInfo.getRequest()), sr -> {
            if (sr.succeeded()) {
                future.complete(sr.result().body());
            } else {
                future.fail(sr.cause());
            }
        });

        return future;
    }

    @Override
    public Future<JsonObject> sendHttpGet(MessageInfoDto messageInfoDto) {
        Future<JsonObject> future = Future.future();

        Handler<Throwable> exHandler = future::fail;
        httpClient.getAbs(messageInfoDto.getUrl(), r -> {
            r.bodyHandler(body -> {
                future.complete(new JsonObject().put("result", body.toString()));
            });
        }).exceptionHandler(exHandler)
//                .putHeader(HttpHeaders.CONTENT_TYPE, "text/xml; charset=UTF-8")
                .end();

        return future;
    }


}
