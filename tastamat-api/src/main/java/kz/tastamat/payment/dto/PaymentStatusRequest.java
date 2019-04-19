package kz.tastamat.payment.dto;

public class PaymentStatusRequest {

	public String pid;

	public static PaymentStatusRequest build(String pid) {
		PaymentStatusRequest payment = new PaymentStatusRequest();
		payment.pid = pid;
		return payment;
	}
}
