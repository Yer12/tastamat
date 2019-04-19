package kz.tastamat.dao.impl;

import kz.tastamat.dao.OrderDao;
import kz.tastamat.db.model.dto.OrderDto;
import kz.tastamat.db.model.enums.OrderStatus;
import kz.tastamat.db.model.enums.Sort;
import kz.tastamat.db.model.jooq.Sequences;
import kz.tastamat.db.model.jooq.tables.JqOrder;
import kz.tastamat.db.model.jooq.tables.records.JqOrderRecord;
import kz.tastamat.db.model.params.SearchParams;
import kz.zx.utils.DateUtils;
import kz.zx.utils.PaginatedList;
import kz.zx.utils.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.SelectQuery;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by baur on 10/18/17.
 */
public class OrderDaoImpl extends JooqDao implements OrderDao {

	private JqOrder o = JqOrder.ORDER;

	public OrderDaoImpl(DSLContext ctx) {
		super(ctx);
	}

	@Override
	public Optional<OrderDto> findById(Long id) {
		return ctx.selectFrom(o).where(o.ID.eq(id)).fetchOptional(OrderDto::build);
	}

	@Override
	public Optional<OrderDto> findByPickCode(String code) {
		return ctx.selectFrom(o).where(o.PICK_CODE.eq(code).and(o.STATUS.in(OrderStatus.RESERVED.name(), OrderStatus.SENT.name()))).fetchOptional(OrderDto::build);
	}

	@Override
	public List<OrderDto> find(SearchParams params) {
		SelectQuery<JqOrderRecord> query = ctx.selectQuery(o);

		if(params.user != null){
			query.addConditions(o.CREATOR.eq(params.user));
		}

		query.addConditions(o.STATUS.ne(OrderStatus.NEW.name()));

		if(StringUtils.isNotEmpty(params.status)){
			query.addConditions(o.STATUS.eq(params.status));
		}

		int count = ctx.fetchCount(query);

		if (StringUtils.isNotEmpty(params.sortBy)) {
			if (params.sortBy.equals("createDate")) {
				if (params.sort.equals(Sort.desc)) {
					query.addOrderBy(o.CREATE_DATE.desc());
				} else {
					query.addOrderBy(o.CREATE_DATE.asc());
				}
			}
		} else {
			query.addOrderBy(o.CREATE_DATE.desc());
		}

		query.addOffset(params.page * params.limit);
		query.addLimit(params.limit);

		List<OrderDto> orders = query.fetch(r -> OrderDto.build(r.into(o)));
		return new PaginatedList<>(orders, (long) count);
	}

	@Override
	public OrderDto create(OrderDto dto) {

		JqOrderRecord record = ctx.newRecord(o);
		record.setId(ctx.nextval(Sequences.ORDER_SEQUENCE));
		record.setCreator(dto.creatorId);
		record.setRecipientName(dto.recipientName);
		record.setRecipientPhone(dto.recipientPhone);
		record.setLockerId(dto.lockerId);
		record.setLockerCode(dto.lockerCode);
//		record.setIdentificator(dto.identificator);
		Optional.ofNullable(dto.size).ifPresent(size -> record.setBoxSize(size.name()));
		record.setStatus(OrderStatus.NEW.name());

		InsertQuery<JqOrderRecord> query = ctx.insertQuery(o);
		query.addRecord(record);
		query.setReturning();
		query.execute();

		return OrderDto.build(query.getReturnedRecord());
	}

	@Override
	public int reserved(String dropCode, String pickCode, Long id) {
		return ctx.update(o)
				.set(o.DROP_CODE, dropCode)
				.set(o.PICK_CODE, pickCode)
				.set(o.STATUS, OrderStatus.RESERVED.name())
				.where(o.ID.eq(id)).execute();
	}

	@Override
	public int drop(String dropCode, String pickCode, Long id) {
		Date current = new Date();
		OffsetDateTime odt = OffsetDateTime.ofInstant(current.toInstant(), ZoneId.systemDefault());
		return ctx.update(o)
				.set(o.DROP_CODE, dropCode)
				.set(o.PICK_CODE, pickCode)
				.set(o.STATUS, OrderStatus.SENT.name())
				.set(o.DROP_DATE, odt)
				.where(o.ID.eq(id)).execute();
	}

	@Override
	public int sent(Long id) {
		Date current = new Date();
		OffsetDateTime odt = OffsetDateTime.ofInstant(current.toInstant(), ZoneId.systemDefault());
		return ctx.update(o).set(o.STATUS, OrderStatus.SENT.name()).set(o.DROP_DATE, odt).where(o.ID.eq(id)).execute();
	}

	@Override
	public int end(Long id) {
		Date current = new Date();
		OffsetDateTime odt = OffsetDateTime.ofInstant(current.toInstant(), ZoneId.systemDefault());
		return ctx.update(o).set(o.STATUS, OrderStatus.END.name()).set(o.PICK_DATE, odt).where(o.ID.eq(id)).execute();
	}

	@Override
	public int sms(Long id) {
		return ctx.update(o).set(o.SMS, true).where(o.ID.eq(id)).execute();
	}

	@Override
	public int withdraw(Long id) {
		Date current = new Date();
		OffsetDateTime odt = OffsetDateTime.ofInstant(current.toInstant(), ZoneId.systemDefault());
		return ctx.update(o).set(o.STATUS, OrderStatus.WITHDRAWN.name()).set(o.PICK_DATE, odt).where(o.ID.eq(id)).execute();
	}

}
