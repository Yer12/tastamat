package kz.tastamat.profile.dto;

import kz.tastamat.db.model.dto.ProfileDto;

public class ProfileInfoDto {

    public Long id;
    public Long wallet;
    public String template;
    public Long user;

    public static ProfileInfoDto build(ProfileDto p) {
        ProfileInfoDto profile = new ProfileInfoDto();
        profile.id = p.id;
        profile.wallet = p.wallet;
        profile.template = p.template;

        return profile;
    }

}
