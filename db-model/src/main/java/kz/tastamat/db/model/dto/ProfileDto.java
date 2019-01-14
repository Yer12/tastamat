package kz.tastamat.db.model.dto;

import kz.tastamat.db.model.jooq.tables.records.JqProfileRecord;

import java.util.Date;
import java.util.Optional;

public class ProfileDto {

	public Long id;
	public Date createDate;
	public Date modifyDate;
	public Long userId;
	public Long wallet;
	public String template;

	public static ProfileDto build(JqProfileRecord p) {
		ProfileDto profile = new ProfileDto();
		profile.id = p.getId();
		profile.createDate = Optional.ofNullable(p.getCreateDate()).map(d -> Date.from(d.toInstant())).orElse(null);
		profile.modifyDate = Optional.ofNullable(p.getModifyDate()).map(d -> Date.from(d.toInstant())).orElse(null);
		profile.userId = p.getUser();
		profile.wallet = p.getWallet();
		profile.template = p.getTemplate();

		return profile;
	}
}
