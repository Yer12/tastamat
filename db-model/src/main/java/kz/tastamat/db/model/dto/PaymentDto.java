package kz.tastamat.db.model.dto;

import kz.tastamat.db.model.enums.PaymentStatus;
import kz.tastamat.db.model.jooq.tables.records.JqPaymentRecord;

import java.util.Date;
import java.util.Optional;

public class PaymentDto {

    public Long id;
    public Date createDate;
    public Date modifyDate;
    public String identificator;
    public Long amount;
    public String pid;
    public Long userId;
    public PaymentStatus status;

    public static PaymentDto build(JqPaymentRecord p) {
        PaymentDto payment = new PaymentDto();
        payment.id = p.getId();
        payment.createDate = Optional.ofNullable(p.getCreateDate()).map(d -> Date.from(d.toInstant())).orElse(null);
        payment.modifyDate = Optional.ofNullable(p.getModifyDate()).map(d -> Date.from(d.toInstant())).orElse(null);
        payment.identificator = p.getIdentificator();
        payment.amount = p.getAmount();
        payment.pid = p.getPid();
        payment.userId = p.getUser();

		Optional.ofNullable(p.getStatus()).map(PaymentStatus::valueOf).ifPresent(status -> payment.status = status);

        return payment;
    }
}
