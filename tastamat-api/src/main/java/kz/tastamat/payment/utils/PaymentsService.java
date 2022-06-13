//package kz.tastamat.payment.utils;
//
//import io.vertx.core.AsyncResult;
//import io.vertx.core.Vertx;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.web.Router;
//import io.vertx.ext.web.RoutingContext;
//import kz.nis.cpi.app.bootstrap.Main;
//import kz.nis.cpi.app.config.ConfigKey;
//import kz.nis.cpi.app.exceptions.BookerException;
//import kz.nis.cpi.booker.beans.PaymentBean;
//import kz.nis.cpi.booker.utils.KKB;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//
///**
// * Created by didar on 1/11/17 deedarb@gmail.com.
// */
//public class PaymentsService extends AbstractService {
//
//	private Logger logger = LogManager.getLogger(PaymentsService.class);
//
//	public PaymentsService(Vertx vertx) {
//		super(vertx);
//	}
//
//	public Router handle(Router router) {
//		router.post("/epayinfo/kkb/success").handler(this::handlePaymentResult);
//		router.post("/epayinfo/kkb/error").handler(this::handlePaymentErrorResult);
//		return router;
//	}
//
//
//	public Router handleSecure(Router router) {
//		router.get("/epayinfo/:orderId").handler(this::handleGetEpayInfo);
//		router.get("/epayinfo/:orderId/status").handler(this::handleGetEpayStatus);
//		return router;
//	}
//
//	private void handleGetEpayInfo(RoutingContext ctx) {
//		String orderId = ctx.pathParam("orderId");
//
//		JsonObject epayConfigs = Main.appConfig.getJsonObject(ConfigKey.EPAY);
//		String base64encoded = KKB.build64(epayConfigs.getString("path") + "/kkbsign.cfg", epayConfigs.getString("price"), ctx.pathParam("orderId"));
//		JsonObject responsePayload = new JsonObject()
//			.put("postLink", epayConfigs.getString("postLink"))
//			.put("postFailureLink", epayConfigs.getString("postFailureLink"))
//			.put("postFailureBackLink", String.format(epayConfigs.getString("postFailureBackLink"), orderId))
//			.put("postBackLink", String.format(epayConfigs.getString("postBackLink"), orderId))
//			.put("orderInfo", base64encoded);
//
//		ok(ctx, responsePayload);
//	}
//
//	private void handlePaymentResult(RoutingContext ctx) {
//		String bodyAsString = ctx.getBodyAsString();
//
//		String xml;
//		try {
//			xml = URLDecoder.decode(bodyAsString.replace("response=", ""), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			throw new RuntimeException( "epay.fail", e);
//		}
//
//		logger.info("payment response xml: {}", xml);
//
//		vertx.executeBlocking(event -> {
//			try {
//				new PaymentBean(ctx).savePaymentInfo(xml);
//				event.complete();
//			} catch (Exception e) {
//				event.fail(e);
//			}
//		}, false, ar -> {
//			if (ar.succeeded()) {
//				ctx.response().end("0");
//			} else {
//				ctx.fail(ar.cause());
//			}
//		});
//	}
//
//	private void handlePaymentErrorResult(RoutingContext ctx) {
//		String bodyAsString = ctx.getBodyAsString();
//		logger.warn("payment error xml: {}", bodyAsString);
//		ctx.response().end("0");
//	}
//
//	private void handleGetEpayStatus(RoutingContext ctx) {
//            String orderId = ctx.pathParam("orderId");
//            vertx.executeBlocking(future -> {
//                try {
//                    String status = new PaymentBean(ctx).getEpayStatusByOrderId(orderId);
//                    future.complete(status);
//                } catch (Exception e) {
//                    future.fail(e);
//                }
//            }, false, (AsyncResult<String> ar) -> {
//                if (ar.succeeded()) {
//                    JsonObject response = new JsonObject();
//                    response.put("status", ar.result());
//
//                    ok(ctx, response);
//                } else {
//                    ctx.fail(ar.cause());
//                }
//            });
//	}
//}
