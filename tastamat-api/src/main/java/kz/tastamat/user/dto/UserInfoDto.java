package kz.tastamat.user.dto;

import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.db.model.enums.Role;
import kz.tastamat.profile.dto.ProfileInfoDto;

public class UserInfoDto {

    public Long id;
    public String phone;
    public String email;
    public Boolean verified;
    public Role role;
    public ProfileInfoDto profile;

    public static UserInfoDto build(UserDto u) {
        UserInfoDto user = new UserInfoDto();
        user.id = u.id;
        user.phone = u.phone;
        user.email = u.email;
        user.verified = u.verified;
        user.role = u.role;

        return user;
    }

}
