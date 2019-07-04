package kz.tastamat.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentStatusResponse {

	public String id;
	public String status;
	public String amount;
	public String email;
	public String date_created;
	public String currency;
	public String used_amount;
	public String refund_amount;
	public Boolean is_kazpost_card;

}
