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

import java.util.Arrays;
import java.util.List;

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
