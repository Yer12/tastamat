package kz.tastamat.payment.dto;

import kz.tastamat.db.model.dto.PaymentDto;

public class PaymentRequest {

	public String amount;
	public String back_link;
	public String payment_webhook;

	public static PaymentRequest build(String amount, String link, String webhook) {
		PaymentRequest payment = new PaymentRequest();
		payment.amount = amount;
		payment.back_link = link;
		payment.payment_webhook = webhook;
		return payment;
	}
}
