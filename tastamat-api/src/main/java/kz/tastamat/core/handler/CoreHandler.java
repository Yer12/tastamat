package kz.tastamat.core.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.core.dto.*;
import kz.tastamat.core.verticle.CoreVerticle;
import kz.tastamat.db.model.dto.OrderDto;
import kz.tastamat.locker.dto.LockerInfoDto;
import kz.tastamat.order.dto.OrderInfoDto;
import kz.zx.api.app.DbHandler;
import kz.zx.api.enums.Key;
import kz.zx.exceptions.ApiException;
import kz.zx.json.Mapper;
import kz.zx.utils.PaginatedList;

import java.util.stream.Collectors;

/**
 * Created by baur on 10/24/17.
 */
public class CoreHandler extends DbHandler {

	private Logger log = LoggerFactory.getLogger(CoreHandler.class);

	private static final DeliveryOptions GET_LOCKERS_ACTION = new DeliveryOptions().addHeader(Key.action, CoreVerticle.Action.GET_LOCKERS.name());
	private static final DeliveryOptions GET_LOCKER_ACTION = new DeliveryOptions().addHeader(Key.action, CoreVerticle.Action.GET_LOCKER.name());
	private static final DeliveryOptions GET_ORDER_ACTION = new DeliveryOptions().addHeader(Key.action, CoreVerticle.Action.GET_ORDER.name());

	private static final DeliveryOptions RESERVE_ACTION = new DeliveryOptions().addHeader(Key.action, CoreVerticle.Action.RESERVE.name());
	private static final DeliveryOptions DROP_ACTION = new DeliveryOptions().addHeader(Key.action, CoreVerticle.Action.DROP.name());
	private static final DeliveryOptions OPEN_ACTION = new DeliveryOptions().addHeader(Key.action, CoreVerticle.Action.OPEN.name());

//	private static final DeliveryOptions INFO_ACTION = new DeliveryOptions().addHeader(Key.action, CoreVerticle.Action.INFO.name());
//	private static final DeliveryOptions PICK_ACTION = new DeliveryOptions().addHeader(Key.action, CoreVerticle.Action.PICK.name());
//	private static final DeliveryOptions RATE_ACTION = new DeliveryOptions().addHeader(Key.action, CoreVerticle.Action.RATE.name());

	public CoreHandler(Vertx vertx) {
		super(vertx);
	}

	public void getLockers(String query, Handler<AsyncResult<PaginatedList<LockerInfoDto>>> handler) {
		CoreRequest request = new CoreRequest();
		request.query = query;
		vertx.eventBus().send(CoreVerticle.ADDRESS, JsonObject.mapFrom(request), GET_LOCKERS_ACTION, (AsyncResult<Message<JsonObject>> ar) -> {
			if (ar.succeeded()) {
				JsonObject result = ar.result().body();
				PaginatedList<LockerInfoDto> list = new PaginatedList(result.getJsonArray("list").stream().map(o -> Mapper.map(LockerInfoDto.class, (JsonObject)o)).collect(Collectors.toList()),
						result.getLong("count"));
				handler.handle(Future.succeededFuture(list));
			} else {
				handler.handle(Future.failedFuture(ar.cause()));
			}
		});
	}

	public void getLocker(Long id, Handler<AsyncResult<LockerInfoDto>> handler) {
		CoreRequest request = new CoreRequest();
		request.id = id;
		vertx.eventBus().send(CoreVerticle.ADDRESS, JsonObject.mapFrom(request), GET_LOCKER_ACTION, (AsyncResult<Message<JsonObject>> ar) -> {
			if (ar.succeeded()) {
				LockerInfoDto locker = Mapper.map(LockerInfoDto.class, ar.result().body());
				handler.handle(Future.succeededFuture(locker));
			} else {
				handler.handle(Future.failedFuture(ar.cause()));
			}
		});
	}

	public void getOrder(String identificator, Handler<AsyncResult<OrderInfoDto>> handler) {
		CoreRequest request = new CoreRequest();
		request.identificator = identificator;
		vertx.eventBus().send(CoreVerticle.ADDRESS, JsonObject.mapFrom(request), GET_ORDER_ACTION, (AsyncResult<Message<JsonObject>> ar) -> {
			if (ar.succeeded()) {
				OrderInfoDto order = Mapper.map(OrderInfoDto.class, ar.result().body());
				handler.handle(Future.succeededFuture(order));
			} else {
				handler.handle(Future.failedFuture(ar.cause()));
			}
		});
	}

	public void reserve(OrderDto order, Handler<AsyncResult<ReserveResponse>> handler) {
		ReserveRequest request = ReserveRequest.build(order);
		vertx.eventBus().send(CoreVerticle.ADDRESS, JsonObject.mapFrom(request), RESERVE_ACTION, (AsyncResult<Message<JsonObject>> ar) -> {
			if (ar.succeeded()) {
				ReserveResponse result = Mapper.map(ReserveResponse.class, ar.result().body());
				handler.handle(Future.succeededFuture(result));
			} else {
				handler.handle(Future.failedFuture(ApiException.unexpected(ar.cause().getMessage())));
			}
		});
	}

	public void drop(OrderDto order, Handler<AsyncResult<DropResponse>> handler) {
		DropRequest request = DropRequest.build(order);
		vertx.eventBus().send(CoreVerticle.ADDRESS, JsonObject.mapFrom(request), DROP_ACTION, (AsyncResult<Message<JsonObject>> ar) -> {
			if (ar.succeeded()) {
				DropResponse result = Mapper.map(DropResponse.class, ar.result().body());
				handler.handle(Future.succeededFuture(result));
			} else {
				handler.handle(Future.failedFuture(ApiException.unexpected(ar.cause().getMessage())));
			}
		});
	}

	public void withdraw(String locker, String pickCode, Handler<AsyncResult<OpenResponse>> handler) {
		OpenRequest request = OpenRequest.pick(locker, pickCode);
		vertx.eventBus().send(CoreVerticle.ADDRESS, JsonObject.mapFrom(request), OPEN_ACTION, (AsyncResult<Message<JsonObject>> ar) -> {
			if (ar.succeeded()) {
				OpenResponse result = Mapper.map(OpenResponse.class, ar.result().body());
				handler.handle(Future.succeededFuture(result));
			} else {
				JsonObject message = new JsonObject(ar.cause().getMessage());
				handler.handle(Future.failedFuture(ApiException.unexpected(message.getString("message"))));
			}
		});
	}

//
//	public void info(String code, Handler<AsyncResult<InfoResponse>> handler) {
//		LockerRequest request = LockerRequest.build(code);
//		vertx.eventBus().send(CoreVerticle.ADDRESS, JsonObject.mapFrom(request), INFO_ACTION, (AsyncResult<Message<JsonObject>> ar) -> {
//			if (ar.succeeded()) {
//				OrderResponse result = Mapper.map(OrderResponse.class, ar.result().body());
//				InfoResponse response = new InfoResponse();
//				response.status = result.status;
//				response.locker = result.locker;
//				response.rating = result.rating;
//				handler.handle(Future.succeededFuture(response));
//			} else {
//				handler.handle(Future.failedFuture(ar.cause()));
//			}
//		});
//	}
//
//	public void pick(OpenRequest openRequest, Handler<AsyncResult<OpenResponse>> handler) {
//		vertx.eventBus().send(CoreVerticle.ADDRESS, JsonObject.mapFrom(openRequest), PICK_ACTION, (AsyncResult<Message<JsonObject>> ar) -> {
//			if (ar.succeeded()) {
//				OpenResponse result = Mapper.map(OpenResponse.class, ar.result().body());
//				handler.handle(Future.succeededFuture(result));
//			} else {
//				handler.handle(Future.failedFuture(ar.cause()));
//			}
//		});
//	}
//
//	public void rate(RateRequest rateRequest, Handler<AsyncResult<RateResponse>> handler) {
//		vertx.eventBus().send(CoreVerticle.ADDRESS, JsonObject.mapFrom(rateRequest), RATE_ACTION, (AsyncResult<Message<JsonObject>> ar) -> {
//			if (ar.succeeded()) {
//				RateResponse result = Mapper.map(RateResponse.class, ar.result().body());
//				handler.handle(Future.succeededFuture(result));
//			} else {
//				handler.handle(Future.failedFuture(ar.cause()));
//			}
//		});
//	}

}
