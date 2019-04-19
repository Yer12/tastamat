package kz.tastamat.core.dto;

import kz.tastamat.db.model.dto.OrderDto;

public class OpenRequest {
    public String code;
    public String locker;

    public static OpenRequest build(String lockerCode, String code) {
        OpenRequest dto = new OpenRequest();
        dto.locker = lockerCode;
        dto.code = code;

        return dto;
    }

    public static OpenRequest drop(OrderDto order) {

        OpenRequest dto = new OpenRequest();
        dto.locker = order.lockerCode;
        dto.code = order.dropCode;

        return dto;
    }

    public static OpenRequest pick(String locker, String pickCode) {

        OpenRequest dto = new OpenRequest();
        dto.locker = locker;
        dto.code = pickCode;

        return dto;
    }
}
