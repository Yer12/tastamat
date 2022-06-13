//package kz.tastamat.payment.utils;
//
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.web.RoutingContext;
//import kz.nis.cpi.app.config.ConfigKey;
//import kz.nis.cpi.app.exceptions.BookerException;
//import kz.nis.cpi.booker.dao.ParticipantDao;
//import kz.nis.cpi.booker.dao.PaymentDao;
//import kz.nis.cpi.booker.model.Sequences;
//import kz.nis.cpi.booker.model.tables.JqPayments;
//import kz.nis.cpi.booker.model.tables.records.JqParticipantsRecord;
//import kz.nis.cpi.booker.model.tables.records.JqPaymentsRecord;
//import kz.nis.cpi.booker.utils.DateUtils;
//import kz.nis.cpi.booker.utils.KKB;
//import kz.nis.cpi.booker.utils.XmlUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.jooq.DSLContext;
//import org.w3c.dom.Document;
//
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathFactory;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * Created by didar on 2/8/17 deedarb@gmail.com.
// */
//public class PaymentBean extends AbstractBean {
//
//	private Logger logger = LogManager.getLogger(PaymentBean.class);
//
//	public PaymentBean(RoutingContext routingContext) {
//		super(routingContext);
//	}
//
//	public String getEpayStatusByOrderId(String orderId) throws SQLException {
//		String username = getUsername().get();
//
//		try (Connection connection = dataSource.getConnection()) {
//			DSLContext jooq = jooq(connection);
//
//			JqParticipantsRecord participantRecord = ParticipantDao.use(jooq).findByCode(orderId).orElseThrow(() -> new BookerException("participant.notFound"));
//			if (participantRecord.getUsername().equals(username) || isAdmin(username, jooq)) {
//				return participantRecord.getStatus();
//			}
//			//JqPaymentsRecord paymentRecord = PaymentDao.use(jooq).findByParticipantId(participantRecord.getId()).orElseThrow(() -> new BookerException("payment.notFound"));
//			throw new BookerException("forbidden");
//		}
//	}
//
//	public void savePaymentInfo(String xml) throws Exception {
//		Document doc = XmlUtils.convert(xml);
//		XPath xPath = XPathFactory.newInstance().newXPath();
//		String orderId = xPath.evaluate("/document/bank/customer/merchant/order/@order_id", doc);
//		String bank_value = xml.substring(10, xml.indexOf("<bank_sign"));
//		String sign = xPath.evaluate("/document/bank_sign", doc);
//
//		JsonObject epayConfigs = config().getJsonObject(ConfigKey.EPAY);
//		String jks = epayConfigs.getString("jks");
//		String alias = epayConfigs.getString("alias");
//		String storepass = epayConfigs.getString("storepass");
//		boolean ok = KKB.verifyResponse(bank_value, sign, epayConfigs.getString("path") + "/" + jks, alias, storepass);
//
//		String status = xPath.evaluate("/document/bank/results/payment/@response_code", doc);
//		String timestamp = xPath.evaluate("/document/bank/results/@timestamp", doc);
//		logger.info("verification: {}, status: {}, timestamp: {}", ok, status, timestamp);
//
//		try (Connection connection = dataSource.getConnection()) {
//			DSLContext jooq = jooq(connection);
//
//			jooq.transaction(tr -> {
//				ParticipantDao.use(jooq).findByCode(orderId).ifPresent(participantRecord -> {
//					JqPaymentsRecord paymentRecord = PaymentDao.use(jooq).findByParticipantId(participantRecord.getId()).orElse(jooq.newRecord(JqPayments.PAYMENTS));
//
//					Timestamp currentTimestamp = DateUtils.currentTimestamp();
//
//					if ("00".equals(status) && ok) {
//						try {
//							Date actualDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
//							paymentRecord.setPaymentActualDate(DateUtils.timestamp(actualDate.getTime()));
//						} catch (ParseException e) {
//							logger.error(e);
//						}
//
//						participantRecord.setStatus(ParticipantDao.Status.WAIT_APPROVE.name());
//						participantRecord.setModifyDate(currentTimestamp);
//						participantRecord.update();
//					}
//					paymentRecord.setParticipantId(participantRecord.getId());
//					paymentRecord.setCode(orderId);
//					paymentRecord.setEpayXml(xml);
//					paymentRecord.setEpayStatus(status);
//					paymentRecord.setModifyDate(currentTimestamp);
//					paymentRecord.setType("epay");
//					paymentRecord.setPaymentDate(currentTimestamp);
//
//					if (paymentRecord.getId() == null) {
//						paymentRecord.setId(jooq.nextval(Sequences.MAIN_SEQUENCE));
//						paymentRecord.setCreateDate(currentTimestamp);
//						paymentRecord.insert();
//					} else {
//						paymentRecord.update();
//					}
//				});
//			});
//		}
//	}
//}
