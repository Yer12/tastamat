package kz.tastamat.payment.bean;

import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.dao.PaymentDao;
import kz.tastamat.dao.ProfileDao;
import kz.tastamat.dao.UserDao;
import kz.tastamat.dao.impl.PaymentDaoImpl;
import kz.tastamat.dao.impl.ProfileDaoImpl;
import kz.tastamat.dao.impl.UserDaoImpl;
import kz.tastamat.db.model.dto.PaymentDto;
import kz.tastamat.db.model.dto.ProfileDto;
import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.db.model.enums.PaymentStatus;
import kz.tastamat.db.model.jooq.Sequences;
import kz.tastamat.db.model.jooq.tables.records.JqPaymentRecord;
import kz.tastamat.db.model.params.SearchParams;
import kz.tastamat.payment.utils.KKB;
import kz.tastamat.utils.EncryptUtils;
import kz.tastamat.utils.XmlUtils;
import kz.zx.exceptions.ApiException;
import kz.zx.utils.DateUtils;
import kz.zx.utils.PaginatedList;
import org.jooq.DSLContext;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by baur on 10/24/17.
 */
public class PaymentBean {

	private final Logger log = LoggerFactory.getLogger(PaymentBean.class);

	private DSLContext ctx;

	public PaymentBean(DSLContext ctx) {
		this.ctx = ctx;
	}

	public static PaymentBean build(DSLContext ctx) {
		return new PaymentBean(ctx);
	}

	public PaginatedList<PaymentDto> find(SearchParams params) {
		return (PaginatedList<PaymentDto>) getPaymentDao(this.ctx).find(params);
	}

	public PaymentDto getFullInfo(Long id) {
		PaymentDto paymentDto = getPaymentDao(this.ctx).findById(id).orElseThrow(() -> ApiException.notFound("payment.not.found"));
		return paymentDto;
	}

	public PaymentDto getFullInfoByIdentifier(String identifier) {
		PaymentDto paymentDto = getPaymentDao(this.ctx).findByIdentificator(identifier).orElseThrow(() -> ApiException.notFound("payment.not.found"));
		return paymentDto;
	}

	public PaymentDto initialize(Long userId, PaymentDto payment) {
		return getPaymentDao(this.ctx).initialize(userId, payment);
	}

	public PaymentDto epayInitialize(Long userId, PaymentDto payment) {
		return getPaymentDao(this.ctx).epayInitialize(userId, payment);
	}

	public PaymentDto create(Long userId, PaymentDto payment) {
		return getPaymentDao(this.ctx).create(userId, payment);
	}

	public int succeeded(Long id) {
		return getPaymentDao(this.ctx).succeeded(id);
	}

	public int pid(Long id, String pid) {
		return getPaymentDao(this.ctx).pid(id, pid);
	}

	public PaymentDto status(Long id, String status) {
		int st = Integer.parseInt(status);
		PaymentStatus stat = PaymentStatus.values()[st];
		return this.ctx.transactionResult(tr -> {
			DSLContext dsl = tr.dsl();
			PaymentDao paymentDao = getPaymentDao(dsl);
			PaymentDto paymentDto = paymentDao.findById(id).orElseThrow(() -> ApiException.notFound("payment.not.found"));
			List<PaymentStatus> statuses = Arrays.asList(PaymentStatus.CONFIRMED, PaymentStatus.APPROVED);
			if (statuses.contains(stat) && !statuses.contains(paymentDto.status)) {
				ProfileDao profileDao = getProfileDao(dsl);
				ProfileDto profileDto = profileDao.findByUser(paymentDto.userId).orElseThrow(() -> ApiException.notFound("profile.not.found"));
				Long wallet = profileDto.wallet + paymentDto.amount;
				profileDao.wallet(profileDto.id, wallet);
				paymentDao.status(id, PaymentStatus.CONFIRMED);
			} else {
				paymentDao.status(id, stat);
			}
			return getPaymentDao(this.ctx).findById(id).orElseThrow(() -> ApiException.notFound("payment.not.found"));
		});
	}

	public PaymentDto epaySuccess(String xml, JsonObject epayConfigs) {
		try {
			Document doc = XmlUtils.convert(xml);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String orderId = xPath.evaluate("/document/bank/customer/merchant/order/@order_id", doc);
			Long paymentId = EncryptUtils.deobfuscate(orderId, 211192, 6);
			String bank_value = xml.substring(10, xml.indexOf("<bank_sign"));
			String sign = xPath.evaluate("/document/bank_sign", doc);

			Boolean verify = epayConfigs.getBoolean("verify");
			String jks = epayConfigs.getString("jks");
			String alias = epayConfigs.getString("alias");
			String storepass = epayConfigs.getString("storepass");
			String path = epayConfigs.getString("path");

			log.info("orderId {}, paymentId {}, bank_value: {}", orderId, paymentId, bank_value);

			log.info("sign: {}", sign);

			log.info("path: {}, jks: {}, alias: {}, storepass: {}", path, jks, alias, storepass);


			boolean ok = KKB.verifyResponse(bank_value, sign, path + "/" + jks, alias, storepass);

			String status = xPath.evaluate("/document/bank/results/payment/@response_code", doc);
			String ref = xPath.evaluate("/document/bank/results/payment/@reference", doc);
			String code = xPath.evaluate("/document/bank/results/payment/@approval_code", doc);

			String timestamp = xPath.evaluate("/document/bank/results/@timestamp", doc);
			log.info("verify: {}, verification: {}, status: {}, timestamp: {}", verify, ok, status, timestamp);

			if ("00".equals(status) && (!verify || ok)) {
				return this.ctx.transactionResult(tr -> {
					DSLContext dsl = tr.dsl();
					PaymentDao paymentDao = getPaymentDao(dsl);

					PaymentDto paymentDto = paymentDao.findById(paymentId).orElseThrow(() -> ApiException.notFound("payment.not.found"));
					if (PaymentStatus.IN_PROCCESS.equals(paymentDto.status)) {
						ProfileDao profileDao = getProfileDao(dsl);
						ProfileDto profileDto = profileDao.findByUser(paymentDto.userId).orElseThrow(() -> ApiException.notFound("profile.not.found"));
						Long wallet = profileDto.wallet + paymentDto.amount;
						profileDao.wallet(profileDto.id, wallet);

						paymentDao.epayApprove(paymentId, ref, code);
					}
					return getPaymentDao(this.ctx).findById(paymentId).orElseThrow(() -> ApiException.notFound("payment.not.found"));
				});
			} else {
				throw ApiException.business("epay.success.error");
			}

		} catch (Exception e) {
			throw ApiException.business("epay.success.error");
		}
	}

	public PaymentDto epayStatus(String xml, JsonObject epayConfigs) {
		try {
			log.info("status xml {}", xml);
			Document doc = XmlUtils.convert(xml);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String orderId = xPath.evaluate("/document/bank/merchant/order/@id", doc);
			Long paymentId = EncryptUtils.deobfuscate(orderId, 211192, 6);
			String bank_value = xml.substring(xml.indexOf("<bank "), xml.indexOf("<bank_sign"));
			String sign = xPath.evaluate("/document/bank_sign", doc);

			Boolean verify = epayConfigs.getBoolean("verify");
			String jks = epayConfigs.getString("jks");
			String alias = epayConfigs.getString("alias");
			String storepass = epayConfigs.getString("storepass");
			String path = epayConfigs.getString("path");

			log.info("orderId {}, paymentId {}, bank_value: {}", orderId, paymentId, bank_value);

			log.info("sign: {}", sign);

			log.info("path: {}, jks: {}, alias: {}, storepass: {}", path, jks, alias, storepass);


			boolean ok = KKB.verifyResponse(bank_value, sign, path + "/" + jks, alias, storepass);

			Boolean payment = Boolean.valueOf(xPath.evaluate("/document/bank/response/@payment", doc));
			String status = xPath.evaluate("/document/bank/response/@status", doc);
			String result = xPath.evaluate("/document/bank/response/@result", doc);
			String ref = xPath.evaluate("/document/bank/response/@reference", doc);
			String code = xPath.evaluate("/document/bank/response/@approval_code", doc);

			log.info("verify: {}, verification: {}, payment: {},  status: {}, result: {}", verify, ok, payment, status, result);

			if (!verify || ok) {
				return this.ctx.transactionResult(tr -> {
					DSLContext dsl = tr.dsl();
					PaymentDao paymentDao = getPaymentDao(dsl);
					PaymentDto paymentDto = paymentDao.findById(paymentId).orElseThrow(() -> ApiException.notFound("payment.not.found"));

					if (PaymentStatus.IN_PROCCESS.equals(paymentDto.status)) {
						ProfileDao profileDao = getProfileDao(dsl);
						if (!payment) {
							paymentDao.status(paymentId, PaymentStatus.FAILED);
						} else {
							if ("0".equals(status) && "0".equals(result)) {
								ProfileDto profileDto = profileDao.findByUser(paymentDto.userId).orElseThrow(() -> ApiException.notFound("profile.not.found"));
								Long wallet = profileDto.wallet + paymentDto.amount;
								profileDao.wallet(profileDto.id, wallet);

								paymentDao.epayApprove(paymentId, ref, code);
							} else if ("2".equals(status) && "0".equals(result)) {
								ProfileDto profileDto = profileDao.findByUser(paymentDto.userId).orElseThrow(() -> ApiException.notFound("profile.not.found"));
								Long wallet = profileDto.wallet + paymentDto.amount;
								profileDao.wallet(profileDto.id, wallet);
								paymentDao.status(paymentId, PaymentStatus.CONFIRMED);
							}
						}
					}
					return getPaymentDao(this.ctx).findById(paymentId).orElseThrow(() -> ApiException.notFound("payment.not.found"));
				});
			} else {
				throw ApiException.business("epay.status.error");
			}
		} catch (Exception e) {
			throw ApiException.business("epay.status.error");
		}
	}

	public PaymentDto epayConfirm(String xml, JsonObject epayConfigs) {
		try {
			Document doc = XmlUtils.convert(xml);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String orderId = xPath.evaluate("/document/bank/merchant/payment/@orderid", doc);
			Long paymentId = EncryptUtils.deobfuscate(orderId, 211192, 6);
			String bank_value = xml.substring(xml.indexOf("<bank "), xml.indexOf("<bank_sign"));
			String sign = xPath.evaluate("/document/bank_sign", doc);

			Boolean verify = epayConfigs.getBoolean("verify");
			String jks = epayConfigs.getString("jks");
			String alias = epayConfigs.getString("alias");
			String storepass = epayConfigs.getString("storepass");
			String path = epayConfigs.getString("path");

			log.info("orderId {}, paymentId {}, bank_value: {}", orderId, paymentId, bank_value);

			log.info("sign: {}", sign);

			log.info("path: {}, jks: {}, alias: {}, storepass: {}", path, jks, alias, storepass);


			boolean ok = KKB.verifyResponse(bank_value, sign, path + "/" + jks, alias, storepass);

			String responseCode = xPath.evaluate("/document/bank/response/@code", doc);
			String message = xPath.evaluate("/document/bank/response/@message", doc);

			log.info("verify: {}, verification: {}, responseCode: {}, message {}", verify, ok, responseCode, message);

			if (!verify || ok) {
				return this.ctx.transactionResult(tr -> {
					DSLContext dsl = tr.dsl();
					PaymentDao paymentDao = getPaymentDao(dsl);
					PaymentDto paymentDto = paymentDao.findById(paymentId).orElseThrow(() -> ApiException.notFound("payment.not.found"));

					if (PaymentStatus.APPROVED.equals(paymentDto.status)) {
						if ("00".equals(responseCode)) {
							paymentDao.status(paymentId, PaymentStatus.CONFIRMED);
						}
					}
					return getPaymentDao(this.ctx).findById(paymentId).orElseThrow(() -> ApiException.notFound("payment.not.found"));
				});
			} else {
				throw ApiException.business("epay.status.error");
			}
		} catch (Exception e) {
			throw ApiException.business("epay.status.error");
		}
	}

	public PaymentDto kaspi(Long profileId, SearchParams params) {
		Optional<PaymentDto> optional = getPaymentDao(ctx).findByPid(String.valueOf(params.txn_id));

		if (optional.isPresent()) {
			return optional.get();
		} else {
			return ctx.transactionResult(tr -> {
				DSLContext dsl = tr.dsl();

				long amount = params.sum.longValue();

				ProfileDao profileDao = getProfileDao(dsl);
				ProfileDto profileDto = profileDao.findById(profileId).orElseThrow(() -> ApiException.notFound("profile.not.found"));
				Long wallet = profileDto.wallet + amount;
				profileDao.wallet(profileDto.id, wallet);

				PaymentDto paymentDto = getPaymentDao(dsl).insert(() -> {
					JqPaymentRecord record = new JqPaymentRecord();
					record.setId(ctx.nextval(Sequences.PAYMENT_SEQUENCE));
					record.setStatus(PaymentStatus.CONFIRMED.name());
					record.setAmount(amount);
					record.setPid(String.valueOf(params.txn_id));
					record.setIdentificator(UUID.randomUUID().toString());
					record.setRef(params.txn_date);
					record.setUser(profileDto.userId);
					return record;
				});

				return paymentDto;
			});
		}
	}

	public PaymentDto vip(String phone, Double sum) {
		return ctx.transactionResult(tr -> {
			DSLContext dsl = tr.dsl();

			long amount = sum.longValue();

			ProfileDao profileDao = getProfileDao(dsl);
			UserDto userDto = getUserDao(dsl).findEnabledByPhone(phone).orElseThrow(() -> ApiException.notFound("user.not.found"));
			ProfileDto profileDto = profileDao.findByUser(userDto.id).orElseThrow(() -> ApiException.notFound("profile.not.found"));
			Long wallet = profileDto.wallet + amount;
			profileDao.wallet(profileDto.id, wallet);

			PaymentDto paymentDto = getPaymentDao(dsl).insert(() -> {
				JqPaymentRecord record = new JqPaymentRecord();
				record.setId(ctx.nextval(Sequences.PAYMENT_SEQUENCE));
				record.setStatus(PaymentStatus.CONFIRMED.name());
				record.setAmount(amount);
				record.setIdentificator(UUID.randomUUID().toString());
				record.setRef("vip");
				record.setUser(profileDto.userId);
				return record;
			});

			return paymentDto;
		});
	}

	private PaymentDao getPaymentDao(DSLContext dsl) {
		return new PaymentDaoImpl(dsl);
	}

	private ProfileDao getProfileDao(DSLContext dsl) {
		return new ProfileDaoImpl(dsl);
	}

	private UserDao getUserDao(DSLContext dsl) {
		return new UserDaoImpl(dsl);
	}
}
