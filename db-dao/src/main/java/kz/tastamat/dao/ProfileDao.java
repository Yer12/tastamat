package kz.tastamat.dao;

import kz.tastamat.db.model.dto.ProfileDto;

import java.util.Optional;

public interface ProfileDao {

    Optional<ProfileDto> findById(Long id);
    Optional<ProfileDto> findByUser(Long userId);

    ProfileDto create(Long userId, Long wallet, String template);
    int template(Long id, String template);
    int wallet(Long id, Long wallet);
}
