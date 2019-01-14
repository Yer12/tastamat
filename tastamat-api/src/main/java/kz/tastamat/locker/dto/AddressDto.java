package kz.tastamat.locker.dto;

public class AddressDto {

	public String regionCode;
	public String regionKk;
	public String regionQq;
	public String regionRu;
	public String regionEn;

	public String districtCode;
	public String districtKk;
	public String districtQq;
	public String districtRu;
	public String districtEn;

	public String streetKk;
	public String streetQq;
	public String streetRu;
	public String streetEn;
	public String streetShortKk;
	public String streetShortQq;
	public String streetShortRu;
	public String streetShortEn;

	public String buildingIndex;
	public String buildingNumber;
	public String buildingKk;
	public String buildingQq;
	public String buildingRu;
	public String buildingEn;
	public String buildingShortKk;
	public String buildingShortQq;
	public String buildingShortRu;
	public String buildingShortEn;

	public AddressDto() {}

//	public static AddressDto build(LockerDto locker) {
//
//		AddressDto dto = new AddressDto();
//
//		dto.regionCode = locker.addressRegionCode;
//		dto.regionKk = locker.addressRegionKk;
//		dto.regionQq = locker.addressRegionQq;
//		dto.regionRu = locker.addressRegionRu;
//		dto.regionEn = locker.addressRegionEn;
//
//		dto.districtCode = locker.addressDistrictCode;
//		dto.districtKk = locker.addressDistrictKk;
//		dto.districtQq = locker.addressDistrictQq;
//		dto.districtRu = locker.addressDistrictRu;
//		dto.districtEn = locker.addressDistrictEn;
//
//		dto.streetKk = locker.addressStreetKk;
//		dto.streetQq = locker.addressStreetQq;
//		dto.streetRu = locker.addressStreetRu;
//		dto.streetEn = locker.addressStreetEn;
//		dto.streetShortKk = locker.addressStreetShortKk;
//		dto.streetShortQq = locker.addressStreetShortQq;
//		dto.streetShortRu = locker.addressStreetShortRu;
//		dto.streetShortEn = locker.addressStreetShortEn;
//
//		dto.buildingIndex = locker.addressBuildingIndex;
//		dto.buildingNumber = locker.addressBuildingNumber;
//		dto.buildingKk = locker.addressBuildingKk;
//		dto.buildingQq = locker.addressBuildingQq;
//		dto.buildingRu = locker.addressBuildingRu;
//		dto.buildingEn = locker.addressBuildingEn;
//		dto.buildingShortKk = locker.addressBuildingShortKk;
//		dto.buildingShortQq = locker.addressBuildingShortQq;
//		dto.buildingShortRu = locker.addressBuildingShortRu;
//		dto.buildingShortEn = locker.addressBuildingShortEn;
//
//		return dto;
//	}
}
