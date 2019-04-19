package kz.tastamat.payment.dto;

import kz.tastamat.db.model.dto.PaymentDto;
import kz.tastamat.db.model.enums.PaymentStatus;

import java.util.Date;

public class PaymentInfoDto {

	public Long id;
	public Date createDate;
	public Long amount;
	public PaymentStatus status;

	public static PaymentInfoDto build(PaymentDto payment){
		PaymentInfoDto dto = new PaymentInfoDto();
		dto.id = payment.id;
		dto.createDate = payment.createDate;
		dto.amount = payment.amount;
		dto.status = payment.status;
		return dto;
	}

}
