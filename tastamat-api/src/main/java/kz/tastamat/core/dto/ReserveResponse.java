package kz.tastamat.core.dto;

import kz.tastamat.db.model.enums.OrderStatus;
import kz.tastamat.locker.dto.LockerInfoDto;

public class ReserveResponse {

	public String dropCode;
	public String pickCode;
	public OrderStatus status;

	public LockerInfoDto locker;

	public Boolean active;
}
