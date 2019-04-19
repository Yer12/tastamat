package kz.tastamat.payment.verticle;

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
import kz.tastamat.payment.dto.PaymentRequest;
import kz.tastamat.payment.dto.PaymentStatusRequest;
import kz.tastamat.utils.HmacUtils;
import kz.zx.api.app.BaseVerticle;
import kz.zx.exceptions.ApiException;
import kz.zx.json.Mapper;

import static kz.zx.api.app.AbstractWebServiceVerticle.APPLICATION_JSON;

public class PaymentVerticle extends BaseVerticle {

    private Logger log = LoggerFactory.getLogger(PaymentVerticle.class);

    public enum Action {
        MAKE_PAYMENT,
        STATUS_PAYMENT
    }

    public static final String ADDRESS = "PaymentVerticle";
    private HttpClient client;
    protected String paypostUrl, token;

    @Override
    public void start() throws Exception {
        super.start();

        JsonObject core = config().getJsonObject(ConfigKey.PAYPOST.key());

        this.paypostUrl = core.getString(ConfigKey.URL.key());
        this.token = core.getString(ConfigKey.TOKEN.key());

        HttpClientOptions options = new HttpClientOptions().setSsl(true).setTrustAll(true);
        this.client = vertx.createHttpClient(options);

        consumer(ADDRESS)
                .on(Action.MAKE_PAYMENT, this::makePayment)
                .on(Action.STATUS_PAYMENT, this::statusPayment)
                .build(Action.class);
    }

    private void makePayment(Message<JsonObject> mes) {

        Handler<Throwable> exHandler = (Throwable ex) -> handle(mes, ex);

        PaymentRequest paymentRequest = Mapper.map(PaymentRequest.class, mes.body());
        String body = JsonObject.mapFrom(paymentRequest).encode();
        log.info("body {}", body);

        client.postAbs(this.paypostUrl+"/api/v0/orders/payment/", rh -> {
            rh.exceptionHandler(exHandler);
            if(201 == rh.statusCode() || 200 == rh.statusCode()){
                rh.bodyHandler(bh -> {
                    JsonObject b = bh.toJsonObject();
                    Boolean success = b.getBoolean("success");
                    if(success){
                        handle(mes, b.getJsonObject("result"));
                    } else {
                        handle(mes, ApiException.unexpected(b.getJsonObject("errors").encode()));
                    }
                });
            } else {
                rh.bodyHandler(bh -> {
                    log.error(bh.toString());
                    JsonObject b = bh.toJsonObject();
                    handle(mes, ApiException.unexpected(b.encode()));
                });
            }
        }).putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .putHeader("Authorization", "Token "+this.token)
                .setChunked(true)
                .write(body)
                .end();
    }

    private void statusPayment(Message<JsonObject> mes) {

        Handler<Throwable> exHandler = (Throwable ex) -> handle(mes, ex);

        PaymentStatusRequest paymentRequest = Mapper.map(PaymentStatusRequest.class, mes.body());

        client.getAbs(this.paypostUrl+"/api/v0/orders/payment/"+paymentRequest.pid+"/", rh -> {
            rh.exceptionHandler(exHandler);
            if(200 == rh.statusCode()){
                rh.bodyHandler(bh -> {
                    JsonObject b = bh.toJsonObject();
                    Boolean success = b.getBoolean("success");
                    if(success){
                        handle(mes, b.getJsonObject("result"));
                    } else {
                        handle(mes, ApiException.unexpected(b.getJsonObject("errors").encode()));
                    }
                });
            } else {
                rh.bodyHandler(bh -> {
                    log.error(bh.toString());
                    JsonObject b = bh.toJsonObject();
                    handle(mes, ApiException.unexpected(b.encode()));
                });
            }
        }).putHeader("Authorization", "Token "+this.token)
                .end();
    }
}
