package kz.tastamat.locker.dto;

import kz.tastamat.db.model.enums.LockerStatus;

import java.util.Date;

public class LockerInfoDto {

	public Long id;
	public Date createDate;
	public Date modifyDate;
	public String code;
	public LockerStatus status;
	public Boolean calibrated;
	public Date infoDate;
	public String index;
	public String oldIndex;
	public Integer size;
	public Integer rows;
	public Integer columns;
	public Object contacts;
	public LocationDto location;
	public AddressDto address;
	public PartnerDto partner;

	public String gisUrl;

	public String presenceCode;

//	public List<BoxDto> boxes;

	public LockerInfoDto() {}

//	public static LockerInfoDto build(LockerDto locker) {
//
//		LockerInfoDto dto = new LockerInfoDto();
//		dto.id = locker.id;
//		dto.createDate = locker.createDate;
//		dto.modifyDate = locker.modifyDate;
//		dto.code = locker.code;
//		dto.status = locker.status;
//		dto.calibrated = locker.calibrated;
//
//		dto.infoDate = locker.infoDate;
//		dto.index = locker.index;
//		dto.oldIndex = locker.oldIndex;
//		dto.size = locker.size;
//		dto.rows = locker.rows;
//		dto.columns = locker.columns;
//		dto.contacts = locker.contacts;
//
//		dto.presenceCode = locker.presenceCode;
//
//		dto.location = LocationDto.build(locker);
//		dto.address = AddressDto.build(locker);
//		dto.partner = PartnerDto.build(locker);
//
//		return dto;
//	}
}
