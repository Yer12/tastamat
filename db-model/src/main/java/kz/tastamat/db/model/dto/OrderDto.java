package kz.tastamat.db.model.dto;

import kz.tastamat.db.model.enums.BoxSize;
import kz.tastamat.db.model.enums.OrderStatus;
import kz.tastamat.db.model.jooq.tables.records.JqOrderRecord;

import java.util.Date;
import java.util.Optional;

public class OrderDto {

	public Long id;
	public Date createDate;
	public Date modifyDate;
	public Long creatorId;
	public UserDto creator;
	public Long lockerId;
	public String lockerCode;
	public String identificator;
	public String recipientName;
	public String recipientPhone;
	public BoxSize size;
	public String dropCode;
	public Date dropDate;
	public String pickCode;
	public Date pickDate;
	public OrderStatus status;
	public Boolean sms;
	public String presenceCode;

	public static OrderDto build(JqOrderRecord order) {

		OrderDto dto = new OrderDto();
		dto.id = order.getId();
		dto.createDate = Optional.ofNullable(order.getCreateDate()).map(d -> Date.from(d.toInstant())).orElse(null);
		dto.modifyDate = Optional.ofNullable(order.getModifyDate()).map(d -> Date.from(d.toInstant())).orElse(null);
		dto.creatorId = order.getCreator();
		dto.identificator = order.getIdentificator();

		dto.recipientName = order.getRecipientName();
		dto.recipientPhone = order.getRecipientPhone();

		dto.lockerId = order.getLockerId();
		dto.lockerCode = order.getLockerCode();
		Optional.ofNullable(order.getBoxSize()).map(BoxSize::valueOf).ifPresent(size -> dto.size = size);
		dto.dropCode = order.getDropCode();
		dto.dropDate = Optional.ofNullable(order.getDropDate()).map(d -> Date.from(d.toInstant())).orElse(null);
		dto.pickCode = order.getPickCode();
		dto.pickDate = Optional.ofNullable(order.getPickDate()).map(d -> Date.from(d.toInstant())).orElse(null);
		Optional.ofNullable(order.getStatus()).map(OrderStatus::valueOf).ifPresent(status -> dto.status = status);

		dto.sms = order.getSms();

		return dto;
	}
}
