package kz.tastamat.order.dto;

import kz.tastamat.db.model.enums.OrderStatus;

import java.util.Date;

public class StatusRequest {

    public Date date;
    public String identificator;
    public OrderStatus status;
}
