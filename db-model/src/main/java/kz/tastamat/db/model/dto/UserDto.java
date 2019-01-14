package kz.tastamat.db.model.dto;

import kz.tastamat.db.model.enums.Role;
import kz.tastamat.db.model.jooq.tables.records.JqUserRecord;

import java.util.Date;
import java.util.Optional;

public class UserDto {

	public Long id;
	public Date createDate;
	public Date modifyDate;
	public Boolean enabled;
	public Boolean confirmed;
	public String phone;
	public String password;
	public String smsCode;
	public Role role;

	public static UserDto build(JqUserRecord u) {
		UserDto user = new UserDto();
		user.id = u.getId();
		user.createDate = Optional.ofNullable(u.getCreateDate()).map(d -> Date.from(d.toInstant())).orElse(null);
		user.modifyDate = Optional.ofNullable(u.getModifyDate()).map(d -> Date.from(d.toInstant())).orElse(null);
		user.confirmed = u.getConfirmed();
		user.enabled = u.getEnabled();
		user.phone = u.getPhone();
		user.smsCode = u.getSmsCode();

//		Optional.ofNullable(u.getRole()).map(Role::valueOf).ifPresent(role -> user.role = role);

		return user;
	}
}
