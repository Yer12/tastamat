package kz.tastamat.dao;

import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.db.model.enums.Role;

import java.util.Optional;

public interface UserDao {

    Optional<UserDto> findById(Long id);

    Optional<UserDto> findByPhone(String phone);

    Optional<UserDto> findEnabledByPhone(String phone);

    Optional<UserDto> findDisabledByPhone(String phone);

    Optional<UserDto> findByPhonePassword(String login, String password);
//
//    Optional<UserDto> findByEmail(String username);

//    boolean hasRole(String email, Role role);

    UserDto clean(Long id, String smsCode);

    UserDto create(String phone, String smsCode);

    UserDto confirm(Long id);

    UserDto enable(Long id, String password);

//    UserDto save(UserDto dto);

}
