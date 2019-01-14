package kz.tastamat.auth.dto;

import kz.tastamat.db.model.dto.UserDto;

public class ConfirmDto {

    public Long id;
    public String phone;
    public String code;
    public String password;
    public Boolean confirmed;

    public static ConfirmDto build(UserDto u) {
        ConfirmDto dto = new ConfirmDto();
        dto.id = u.id;
        dto.phone = u.phone;
        dto.confirmed = u.confirmed;

        return dto;
    }
}
