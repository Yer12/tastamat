package kz.tastamat.payment.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class PaymentKaspiResponse {

    public Long txn_id;
    public Long prv_txn;
    public Double sum;
    public Long result;
    public String comment;
}
