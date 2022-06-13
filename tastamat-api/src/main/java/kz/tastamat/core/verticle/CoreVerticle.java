package kz.tastamat.core.verticle;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.core.dto.CoreRequest;
import kz.tastamat.core.dto.DropRequest;
import kz.tastamat.core.dto.OpenRequest;
import kz.tastamat.core.dto.ReserveRequest;
import kz.tastamat.enums.ConfigKey;
import kz.tastamat.utils.HmacUtils;
import kz.zx.api.app.BaseVerticle;
import kz.zx.exceptions.ApiException;
import kz.zx.json.Mapper;

import static kz.zx.api.app.AbstractWebServiceVerticle.APPLICATION_JSON;

public class CoreVerticle extends BaseVerticle {

    private Logger log = LoggerFactory.getLogger(CoreVerticle.class);

    public enum Action {
        GET_LOCKERS,
        GET_LOCKER,
        GET_ORDER,
        RESERVE,
        DROP,
        OPEN,
        WITHDRAW,
        TRACKING,
        INFO,
        PICK,
        RATE
    }

    public static final String ADDRESS = "CoreVerticle";
    private HttpClient client;
    protected String coreUrl, key, token;

    @Override
    public void start() throws Exception {
        super.start();

        JsonObject core = config().getJsonObject(ConfigKey.CORE.key());

        this.coreUrl = core.getString(ConfigKey.URL.key());
        this.key = core.getString(ConfigKey.KEY.key());
        this.token = core.getString(ConfigKey.TOKEN.key());

        HttpClientOptions options = new HttpClientOptions().setSsl(true).setTrustAll(true);
        this.client = vertx.createHttpClient(options);

        consumer(ADDRESS)
                .on(Action.GET_LOCKERS, this::getLockers)
                .on(Action.GET_LOCKER, this::getLocker)
                .on(Action.GET_ORDER, this::getOrder)
                .on(Action.RESERVE, this::reserve)
                .on(Action.DROP, this::drop)
                .on(Action.OPEN, this::open)
                .on(Action.WITHDRAW, this::withdraw)
                .on(Action.TRACKING, this::getTracking)
//                .on(Action.INFO, this::handleGetInfo)
//                .on(Action.PICK, this::handlePick)
//                .on(Action.RATE, this::handleRate)
                .build(Action.class);
    }

    private void getLockers(Message<JsonObject> mes){
        CoreRequest coreRequest = Mapper.map(CoreRequest.class, mes.body());
        Handler<Throwable> exHandler = (Throwable ex) -> handle(mes, ex);
        client.getAbs(coreUrl+"/lockers"+coreRequest.query, rh -> {
            rh.exceptionHandler(exHandler);
            if(200 == rh.statusCode()){
                rh.bodyHandler(bh -> {
                    JsonObject b = bh.toJsonObject();
                    handle(mes, b);
                });
            } else {
                rh.bodyHandler(bh -> {
                    log.error(bh.toString());
                    JsonObject b = bh.toJsonObject();
                    JsonObject error = new JsonObject(b.getString("message"));
                    handle(mes, ApiException.unexpected(error.encode()));
                });
            }
        }).putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON).end();
    }

    private void getLocker(Message<JsonObject> mes){
        CoreRequest coreRequest = Mapper.map(CoreRequest.class, mes.body());

        Handler<Throwable> exHandler = (Throwable ex) -> handle(mes, ex);
        client.getAbs(coreUrl+"/lockers/"+coreRequest.id, rh -> {
            rh.exceptionHandler(exHandler);
            if(200 == rh.statusCode()){
                rh.bodyHandler(bh -> {
                    JsonObject b = bh.toJsonObject();
                    handle(mes, b);
                });
            } else {
                rh.bodyHandler(bh -> {
                    log.error(bh.toString());
                    JsonObject b = bh.toJsonObject();
                    JsonObject error = new JsonObject(b.getString("message"));
                    handle(mes, ApiException.unexpected(error.encode()));
                });
            }
        }).putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON).end();
    }

    private void getOrder(Message<JsonObject> mes){
        CoreRequest coreRequest = Mapper.map(CoreRequest.class, mes.body());

        Handler<Throwable> exHandler = (Throwable ex) -> handle(mes, ex);
        client.getAbs(coreUrl+"/administartion/orders/"+coreRequest.identificator, rh -> {
            rh.exceptionHandler(exHandler);
            if(200 == rh.statusCode()){
                rh.bodyHandler(bh -> {
                    JsonObject b = bh.toJsonObject();
                    handle(mes, b);
                });
            } else {
                rh.bodyHandler(bh -> {
                    log.error(bh.toString());
                    JsonObject b = bh.toJsonObject();
                    JsonObject error = new JsonObject(b.getString("message"));
                    handle(mes, ApiException.unexpected(error.encode()));
                });
            }
        }).putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON).end();
    }

    private void reserve(Message<JsonObject> mes) {

        Handler<Throwable> exHandler = (Throwable ex) -> handle(mes, ex);

        ReserveRequest reserveDto = Mapper.map(ReserveRequest.class, mes.body());
        String body = JsonObject.mapFrom(reserveDto).toString();
        String hmac = HmacUtils.hmacSign(token, body);
        client.postAbs(coreUrl+"/i/orders/reserve", rh -> {
            rh.exceptionHandler(exHandler);
            if(200 == rh.statusCode()){
                rh.bodyHandler(bh -> {
                    JsonObject b = bh.toJsonObject();
                    handle(mes, b);
                });
            } else {
                rh.bodyHandler(bh -> {
                    log.error(bh.toString());
                    JsonObject b = bh.toJsonObject();
                    JsonObject error = new JsonObject(b.getString("message"));
                    handle(mes, ApiException.unexpected(error.encode()));
                });
            }
        }).putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .putHeader("x-hmac-key",key)
                .putHeader("x-hmac", hmac)
                .setChunked(true)
                .write(body)
                .end();
    }

    private void drop(Message<JsonObject> mes){
        Handler<Throwable> exHandler = (Throwable ex) -> handle(mes, ex);

        DropRequest dropRequest = Mapper.map(DropRequest.class, mes.body());
        dropRequest.locker = dropRequest.locker.toUpperCase();
        String body = JsonObject.mapFrom(dropRequest).toString();
        String hmac = HmacUtils.hmacSign(token, body);
        client.postAbs(coreUrl+"/i/orders/book-drop", rh -> {
            rh.exceptionHandler(exHandler);
            if(200 == rh.statusCode()){
                rh.bodyHandler(bh -> {
                    JsonObject b = bh.toJsonObject();
                    handle(mes, b);
                });
            } else {
                rh.bodyHandler(bh -> {
                    log.error(bh.toString());
                    JsonObject b = bh.toJsonObject();
                    JsonObject error = new JsonObject(b.getString("message"));
                    handle(mes, ApiException.unexpected(error.encode()));
                });
            }
        }).putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .putHeader("x-hmac-key",key)
                .putHeader("x-hmac", hmac)
                .setChunked(true)
                .write(body)
                .end();
    }

    private void open(Message<JsonObject> mes){
        Handler<Throwable> exHandler = (Throwable ex) -> handle(mes, ex);

        OpenRequest openRequest = Mapper.map(OpenRequest.class, mes.body());
        openRequest.code = openRequest.code.toUpperCase();
        openRequest.locker = openRequest.locker.toUpperCase();
        String body = JsonObject.mapFrom(openRequest).toString();
        String hmac = HmacUtils.hmacSign(token, body);
        client.putAbs(coreUrl+"/i/orders/open", rh -> {
            rh.exceptionHandler(exHandler);
            if(200 == rh.statusCode()){
                rh.bodyHandler(bh -> {
                    JsonObject b = bh.toJsonObject();
                    handle(mes, b);
                });
            } else {
                rh.bodyHandler(bh -> {
                    log.error(bh.toString());
                    JsonObject b = bh.toJsonObject();
                    JsonObject error = new JsonObject(b.getString("message"));
                    handle(mes, ApiException.unexpected(error.encode()));
                });
            }
        }).putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .putHeader("x-hmac-key",key)
                .putHeader("x-hmac", hmac)
                .setChunked(true)
                .write(body)
                .end();
    }

    private void withdraw(Message<JsonObject> mes){
        Handler<Throwable> exHandler = (Throwable ex) -> handle(mes, ex);

        OpenRequest openRequest = Mapper.map(OpenRequest.class, mes.body());
        openRequest.code = openRequest.code.toUpperCase();
        openRequest.locker = openRequest.locker.toUpperCase();
        String body = JsonObject.mapFrom(openRequest).toString();
        String hmac = HmacUtils.hmacSign(token, body);
        client.putAbs(coreUrl+"/i/orders/withdraw", rh -> {
            rh.exceptionHandler(exHandler);
            if(200 == rh.statusCode()){
                rh.bodyHandler(bh -> {
                    JsonObject b = bh.toJsonObject();
                    handle(mes, b);
                });
            } else {
                rh.bodyHandler(bh -> {
                    log.error(bh.toString());
                    JsonObject b = bh.toJsonObject();
                    JsonObject error = new JsonObject(b.getString("message"));
                    handle(mes, ApiException.unexpected(error.encode()));
                });
            }
        }).putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .putHeader("x-hmac-key",key)
                .putHeader("x-hmac", hmac)
                .setChunked(true)
                .write(body)
                .end();
    }

    private void getTracking(Message<JsonObject> mes){
        Handler<Throwable> exHandler = (Throwable ex) -> handle(mes, ex);
        client.getAbs(coreUrl+"/orders/tracking/"+mes.body().getString("identifier"), rh -> {
            rh.exceptionHandler(exHandler);
            if(200 == rh.statusCode()){
                rh.bodyHandler(bh -> {
                    JsonObject b = bh.toJsonObject();
                    handle(mes, b);
                });
            } else {
                rh.bodyHandler(bh -> {
                    log.error(bh.toString());
                    JsonObject b = bh.toJsonObject();
                    JsonObject error = new JsonObject(b.getString("message"));
                    handle(mes, ApiException.unexpected(error.encode()));
                });
            }
        }).putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON).end();
    }
}
