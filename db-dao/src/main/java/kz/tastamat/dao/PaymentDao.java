package kz.tastamat.dao;

import kz.tastamat.db.model.dto.PaymentDto;
import kz.tastamat.db.model.enums.PaymentStatus;
import kz.tastamat.db.model.params.SearchParams;

import java.util.List;
import java.util.Optional;

public interface PaymentDao {

    Optional<PaymentDto> findById(Long id);
    Optional<PaymentDto> findByIdentificator(String identificator);
    PaymentDto create(PaymentDto dto);
    List<PaymentDto> find(SearchParams params);

    int pid(Long id, String pid);
    int approve(String identificator);
    int status(Long id, PaymentStatus status);
}
