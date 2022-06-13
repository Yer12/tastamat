package kz.tastamat.application.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import kz.tastamat.application.dto.ApplicationCreateRequest;
import kz.tastamat.core.dto.DropResponse;
import kz.tastamat.core.handler.CoreHandler;
import kz.tastamat.db.model.dto.OrderDto;
import kz.tastamat.db.model.dto.PaymentDto;
import kz.tastamat.db.model.enums.OrderStatus;
import kz.tastamat.db.model.enums.PaymentStatus;
import kz.tastamat.db.model.params.SearchParams;
import kz.tastamat.enums.ConfigKey;
import kz.tastamat.order.bean.OrderBean;
import kz.tastamat.order.dto.ActionDto;
import kz.tastamat.order.dto.OrderInfoDto;
import kz.tastamat.order.dto.StatusRequest;
import kz.tastamat.order.dto.StatusResponse;
import kz.tastamat.payment.handler.PaymentHandler;
import kz.tastamat.profile.bean.ProfileBean;
import kz.tastamat.profile.dto.ProfileInfoDto;
import kz.tastamat.sms.dto.SmsDto;
import kz.tastamat.sms.handler.SmsHandler;
import kz.tastamat.utils.JsonUtils;
import kz.tastamat.utils.QueryParamsUtils;
import kz.zx.api.app.DbHandler;
import kz.zx.exceptions.ApiException;
import kz.zx.utils.Holder;
import kz.zx.utils.PaginatedList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

public class ApplicationHandler extends DbHandler {

	private Logger log = LoggerFactory.getLogger(ApplicationHandler.class);

	private WebClient webClient;

	public ApplicationHandler(Vertx vertx) {
		super(vertx);
		this.webClient = WebClient.create(vertx);
	}

	public Future<JsonObject> createApplication(ApplicationCreateRequest req) {
		Future<JsonObject> future = Future.future();
		HttpRequest<JsonObject> request = webClient.postAbs("https://api.courier.tastamat.com/api/v1/yandex/delivery/create?appKey=Crtw2bNiJGdj3HQwNrJBCYHVzHa0Of2l")
				.as(BodyCodec.jsonObject());

		request.sendJson(JsonObject.mapFrom(req), sr -> {
			if (sr.succeeded()) {
				future.complete(sr.result().body());
			} else {
				future.fail(sr.cause());
			}
		});

		return future;
	}

	public Future<JsonObject> checkPrice(String identifier) {
		Future<JsonObject> future = Future.future();
		HttpRequest<JsonObject> request = webClient.getAbs("https://api.courier.tastamat.com/api/v1/yandex/order/price?appKey=Crtw2bNiJGdj3HQwNrJBCYHVzHa0Of2l&order="+identifier)
				.as(BodyCodec.jsonObject());

		request.send(sr -> {
			if (sr.succeeded()) {
				future.complete(sr.result().body());
			} else {
				future.fail(sr.cause());
			}
		});

		return future;
	}

	public Future<JsonObject> getList(MultiMap params) {

		Holder<String> holder = new Holder<>();
		holder.set("?appKey=Crtw2bNiJGdj3HQwNrJBCYHVzHa0Of2l");

		params.iterator().forEachRemaining(stringStringEntry -> {
			String s = holder.get();
			s += "&";
			String key = stringStringEntry.getKey();
			String value = stringStringEntry.getValue();
			s += key+"="+value;
			holder.set(s);
		});

		Future<JsonObject> future = Future.future();
		HttpRequest<JsonObject> request = webClient.getAbs("https://api.courier.tastamat.com/api/v1/yandex/order/tracking/orders"+holder.get())
				.as(BodyCodec.jsonObject());

		request.send(sr -> {
			if (sr.succeeded()) {
				future.complete(sr.result().body());
			} else {
				future.fail(sr.cause());
			}
		});

		return future;
	}

	public Future<JsonObject> getById(String identifier) {
		Future<JsonObject> future = Future.future();
		HttpRequest<JsonObject> request = webClient.getAbs("https://api.courier.tastamat.com/api/v1/yandex/order/tracking/info?appKey=Crtw2bNiJGdj3HQwNrJBCYHVzHa0Of2l&order="+identifier)
				.as(BodyCodec.jsonObject());

		request.send(sr -> {
			if (sr.succeeded()) {
				future.complete(sr.result().body());
			} else {
				future.fail(sr.cause());
			}
		});

		return future;
	}
}
