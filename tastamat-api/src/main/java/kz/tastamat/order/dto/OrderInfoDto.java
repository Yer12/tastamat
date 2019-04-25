package kz.tastamat.order.dto;

import kz.tastamat.db.model.enums.OrderStatus;
import kz.tastamat.locker.dto.LockerInfoDto;
import kz.tastamat.system.dto.SystemDto;

import java.util.Date;

public class OrderInfoDto {
	public Long id;
	public Date createDate;
	public Date modifyDate;
	public SystemDto created;

	public String identificator;

	public String dropCode;
	public Date dropDate;
	public SystemDto droped;

	public String pickCode;
	public Date pickDate;
	public SystemDto picked;

	public LockerInfoDto locker;
//	public BoxDto box;
	public OrderStatus status;

	public Boolean active;

//	public static OrderInfoDto build(OrderDto order) {
//
//		OrderInfoDto dto = new OrderInfoDto();
//		dto.id = order.id;
//		dto.createDate = order.createDate;
//		dto.modifyDate = order.modifyDate;
//
//		dto.identificator = order.identificator;
//
//		dto.dropCode = order.dropCode;
//		dto.dropDate = order.dropDate;
//
//		dto.pickDate = order.pickDate;
//		dto.pickCode = order.pickCode;
//
//		dto.status = order.status;
//
//		return dto;
//	}
}
