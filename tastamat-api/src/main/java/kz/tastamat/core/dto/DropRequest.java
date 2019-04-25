package kz.tastamat.core.dto;

import kz.tastamat.db.model.dto.OrderDto;
import kz.tastamat.db.model.enums.BoxSize;

public class DropRequest {

	public String identificator;
	public String recipientName;
	public String recipientPhone;
	public String locker;
	public BoxSize size;

	public static DropRequest build(OrderDto order) {

		DropRequest dto = new DropRequest();
		dto.locker = order.presenceCode;
		dto.size = order.size;
		dto.recipientName = order.recipientName;
		dto.recipientPhone = order.recipientPhone;
		dto.identificator = order.identificator;

		return dto;
	}
}
