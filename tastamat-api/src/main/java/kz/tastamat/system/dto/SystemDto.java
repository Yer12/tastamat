package kz.tastamat.system.dto;

import java.util.Date;

public class SystemDto {

	public Long id;
	public Date createDate;
	public Date modifyDate;
	public String key;
	public String token;

	public SystemDto() {}

	public SystemDto(Long id) {
		this.id = id;
	}

//	public static SystemDto build(JqSystemRecord system) {
//
//		SystemDto dto = new SystemDto();
//		dto.id = system.getId();
//		dto.createDate = Optional.ofNullable(system.getCreateDate()).map(d -> Date.from(d.toInstant())).orElse(null);
//		dto.modifyDate = Optional.ofNullable(system.getModifyDate()).map(d -> Date.from(d.toInstant())).orElse(null);
//		dto.key = system.getKey();
//		dto.token = system.getToken();
//
//		return dto;
//	}
}
