package kz.tastamat.auth.dto;

import kz.tastamat.user.dto.UserInfoDto;

public class ConfirmDto {

    public Long id;
    public String phone;
    public String email;
    public String code;
    public String password;

    public static ConfirmDto build(UserInfoDto u) {
        ConfirmDto dto = new ConfirmDto();
        dto.id = u.id;
        dto.phone = u.phone;
        dto.email = u.email;

        return dto;
    }
}
