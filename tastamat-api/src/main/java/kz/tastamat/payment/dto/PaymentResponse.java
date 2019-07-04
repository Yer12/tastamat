package kz.tastamat.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponse {

	public Long id;
	public String amount;
	public String payment;
	public String url;

}
