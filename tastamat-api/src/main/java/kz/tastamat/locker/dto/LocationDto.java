package kz.tastamat.locker.dto;

import kz.tastamat.locker.enums.LocationAccess;
import kz.tastamat.locker.enums.LocationType;

public class LocationDto {

	public LocationType type;
	public String time;
	public LocationAccess access;
	public Double latitude;
	public Double longitude;
	public String descriptionKk;
	public String descriptionQq;
	public String descriptionRu;
	public String descriptionEn;

	public Long distance;

	public LocationDto() {}

//	public static LocationDto build(LockerDto locker) {
//
//		LocationDto dto = new LocationDto();
//		dto.type = locker.locationType;
//		dto.time = locker.locationTime;
//		dto.access = locker.locationAccess;
//		dto.latitude = locker.locationLatitude;
//		dto.longitude = locker.locationLongitude;
//		dto.descriptionKk = locker.locationDescriptionKk;
//		dto.descriptionQq = locker.locationDescriptionQq;
//		dto.descriptionRu = locker.locationDescriptionRu;
//		dto.descriptionEn = locker.locationDescriptionEn;
//		dto.distance = locker.locationDistance;
//
//		return dto;
//	}
}
