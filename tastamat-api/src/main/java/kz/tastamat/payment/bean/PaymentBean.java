package kz.tastamat.payment.bean;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.dao.PaymentDao;
import kz.tastamat.dao.ProfileDao;
import kz.tastamat.dao.impl.PaymentDaoImpl;
import kz.tastamat.dao.impl.ProfileDaoImpl;
import kz.tastamat.db.model.dto.PaymentDto;
import kz.tastamat.db.model.dto.ProfileDto;
import kz.tastamat.db.model.enums.PaymentStatus;
import kz.tastamat.db.model.params.SearchParams;
import kz.zx.exceptions.ApiException;
import kz.zx.utils.PaginatedList;
import org.jooq.DSLContext;

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

	public PaymentDto create(PaymentDto payment) {
		return getPaymentDao(this.ctx).create(payment);
	}

	public int pid(Long id, String pid) {
		return getPaymentDao(this.ctx).pid(id, pid);
	}

	public int approve(String identificator) {
		return this.ctx.transactionResult(tr -> {
			DSLContext dsl = tr.dsl();
			PaymentDao paymentDao = getPaymentDao(dsl);
			ProfileDao profileDao = getProfileDao(dsl);
			PaymentDto paymentDto = paymentDao.findByIdentificator(identificator).orElseThrow(() -> ApiException.notFound("payment.not.found"));
			if(!PaymentStatus.IN_PROCCESS.equals(paymentDto.status)){
				throw ApiException.business("payment.finished");
			}
			ProfileDto profileDto = profileDao.findByUser(paymentDto.userId).orElseThrow(() -> ApiException.notFound("profile.not.found"));
			log.info("paymentDto.id {}", paymentDto.id);
			log.info("profileDto.wallet {}", profileDto.wallet);
			log.info("paymentDto.amount {}", paymentDto.amount);
			Long wallet = profileDto.wallet+paymentDto.amount;
			log.info("wallet {}", wallet);
			profileDao.wallet(profileDto.id, wallet);
			return paymentDao.approve(identificator);
		});
	}

	public PaymentDto status(Long id, String status) {
		int st = Integer.parseInt(status);
		PaymentStatus stat = PaymentStatus.values()[st];
		return this.ctx.transactionResult(tr -> {
			DSLContext dsl = tr.dsl();
			PaymentDao paymentDao = getPaymentDao(dsl);
			PaymentDto paymentDto = paymentDao.findById(id).orElseThrow(() -> ApiException.notFound("payment.not.found"));
			if(PaymentStatus.APPROVED.equals(stat)){
				ProfileDao profileDao = getProfileDao(dsl);
				ProfileDto profileDto = profileDao.findByUser(paymentDto.userId).orElseThrow(() -> ApiException.notFound("profile.not.found"));
				log.info("paymentDto.id {}", paymentDto.id);
				log.info("profileDto.wallet {}", profileDto.wallet);
				log.info("paymentDto.amount {}", paymentDto.amount);
				Long wallet = profileDto.wallet+paymentDto.amount;
				log.info("wallet {}", wallet);
				profileDao.wallet(profileDto.id, wallet);
			}
			paymentDao.status(id, stat);
			return getPaymentDao(this.ctx).findById(id).orElseThrow(() -> ApiException.notFound("payment.not.found"));
		});
	}

	private PaymentDao getPaymentDao(DSLContext dsl) {
		return new PaymentDaoImpl(dsl);
	}

	private ProfileDao getProfileDao(DSLContext dsl) {
		return new ProfileDaoImpl(dsl);
	}
}
