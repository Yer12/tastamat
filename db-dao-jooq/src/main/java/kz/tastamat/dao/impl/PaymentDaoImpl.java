package kz.tastamat.dao.impl;

import kz.tastamat.dao.PaymentDao;
import kz.tastamat.db.model.dto.PaymentDto;
import kz.tastamat.db.model.enums.OrderStatus;
import kz.tastamat.db.model.enums.PaymentStatus;
import kz.tastamat.db.model.enums.Sort;
import kz.tastamat.db.model.jooq.Sequences;
import kz.tastamat.db.model.jooq.tables.JqPayment;
import kz.tastamat.db.model.jooq.tables.records.JqOrderRecord;
import kz.tastamat.db.model.jooq.tables.records.JqPaymentRecord;
import kz.tastamat.db.model.params.SearchParams;
import kz.zx.utils.DateUtils;
import kz.zx.utils.PaginatedList;
import kz.zx.utils.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.SelectQuery;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by baur on 10/18/17.
 */
public class PaymentDaoImpl extends JooqDao implements PaymentDao {

	private JqPayment p = JqPayment.PAYMENT;

	public PaymentDaoImpl(DSLContext ctx) {
		super(ctx);
	}

	@Override
	public Optional<PaymentDto> findById(Long id) {
		return ctx.selectFrom(p).where(p.ID.eq(id)).fetchOptional(PaymentDto::build);
	}

	@Override
	public Optional<PaymentDto> findByIdentificator(String identificator) {
		return ctx.selectFrom(p).where(p.IDENTIFICATOR.eq(identificator)).fetchOptional(PaymentDto::build);
	}

	@Override
	public PaymentDto create(PaymentDto dto) {

		JqPaymentRecord record = ctx.newRecord(p);
		record.setId(ctx.nextval(Sequences.PAYMENT_SEQUENCE));
		record.setIdentificator(UUID.randomUUID().toString());
		record.setAmount(dto.amount);
		record.setUser(dto.userId);
		record.setStatus(OrderStatus.NEW.name());

		InsertQuery<JqPaymentRecord> query = ctx.insertQuery(p);
		query.addRecord(record);
		query.setReturning();
		query.execute();

		return PaymentDto.build(query.getReturnedRecord());
	}

	@Override
	public List<PaymentDto> find(SearchParams params) {
		SelectQuery<JqPaymentRecord> query = ctx.selectQuery(p);

		if(params.user != null){
			query.addConditions(p.USER.eq(params.user));
		}

		int count = ctx.fetchCount(query);

		if (StringUtils.isNotEmpty(params.sortBy)) {
			if (params.sortBy.equals("createDate")) {
				if (params.sort.equals(Sort.desc)) {
					query.addOrderBy(p.CREATE_DATE.desc());
				} else {
					query.addOrderBy(p.CREATE_DATE.asc());
				}
			}
		} else {
			query.addOrderBy(p.CREATE_DATE.desc());
		}

		query.addOffset(params.page * params.limit);
		query.addLimit(params.limit);

		List<PaymentDto> payments = query.fetch(r -> PaymentDto.build(r.into(p)));
		return new PaginatedList<>(payments, (long) count);
	}

	@Override
	public int pid(Long id, String pid) {
		return ctx.update(p).set(p.STATUS, PaymentStatus.IN_PROCCESS.name()).set(p.PID, pid).where(p.ID.eq(id)).execute();
	}

	@Override
	public int approve(String identificator) {
		return ctx.update(p).set(p.STATUS, PaymentStatus.APPROVED.name()).where(p.IDENTIFICATOR.eq(identificator)).execute();
	}

	@Override
	public int status(Long id, PaymentStatus status) {
		return ctx.update(p).set(p.STATUS, status.name()).where(p.ID.eq(id)).execute();
	}

}
