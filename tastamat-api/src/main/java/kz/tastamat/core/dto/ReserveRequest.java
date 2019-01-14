package kz.tastamat.core.dto;

import kz.tastamat.db.model.dto.OrderDto;
import kz.tastamat.db.model.enums.BoxSize;

public class ReserveRequest {

	public String locker;
	public BoxSize size;

	public static ReserveRequest build(OrderDto order) {

		ReserveRequest dto = new ReserveRequest();
		dto.locker = order.lockerCode;
		dto.size = order.size;

		return dto;
	}
}
