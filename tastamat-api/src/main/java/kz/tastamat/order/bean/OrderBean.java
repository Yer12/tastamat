package kz.tastamat.order.bean;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.dao.OrderDao;
import kz.tastamat.dao.impl.OrderDaoImpl;
import kz.tastamat.db.model.dto.OrderDto;
import kz.zx.exceptions.ApiException;
import kz.zx.utils.PaginatedList;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

/**
 * Created by baur on 10/24/17.
 */
public class OrderBean {

	private final Logger log = LoggerFactory.getLogger(OrderBean.class);

	private DSLContext ctx;

	public OrderBean(DSLContext ctx) {
		this.ctx = ctx;
	}

	public static OrderBean build(DSLContext ctx) {
		return new OrderBean(ctx);
	}

	public OrderDto getFullInfo(Long id) {
		OrderDto orderDto = getOrderDao(this.ctx).findById(id).orElseThrow(() -> ApiException.notFound("order.not.found"));
		return orderDto;
	}

	public OrderDto create(OrderDto order) {
		return getOrderDao(this.ctx).create(order);
	}

	public int reserved(String dropCode, String pickCode, Long id) {
		return getOrderDao(this.ctx).reserved(dropCode, pickCode, id);
	}

	public int sent(Long id) {
		return getOrderDao(this.ctx).sent(id);
	}

	public int drop(String dropCode, String pickCode, Long id) {
		return getOrderDao(this.ctx).drop(dropCode, pickCode, id);
	}

	public int sms(Long id) {
		return getOrderDao(this.ctx).sms(id);
	}

	private OrderDao getOrderDao(DSLContext dsl) {
		return new OrderDaoImpl(dsl);
	}
}
