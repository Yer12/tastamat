package kz.tastamat.order.dto;

import java.util.Date;

public class StatusResponse {

    public Date date;
    public Result result;

    public enum Result {
        SUCCESS, ERROR
    }
}
