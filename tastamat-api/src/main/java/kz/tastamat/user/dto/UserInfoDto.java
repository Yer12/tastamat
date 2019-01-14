package kz.tastamat.user.dto;

import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.db.model.enums.Role;

public class UserInfoDto {

    public Long id;
    public String phone;
    public Role role;

    public static UserInfoDto build(UserDto u) {
        UserInfoDto user = new UserInfoDto();
        user.id = u.id;
        user.phone = u.phone;

        return user;
    }

}
