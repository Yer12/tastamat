package kz.tastamat.core.dto;

import kz.tastamat.db.model.dto.LockerDto;
import kz.tastamat.db.model.enums.OrderStatus;

public class OpenResponse {

	public String dropCode;
	public String pickCode;
	public OrderStatus status;

	public LockerDto locker;

	public Boolean active;
}
