package kz.tastamat.dao;

import kz.tastamat.db.model.dto.PaymentDto;
import kz.tastamat.db.model.enums.PaymentStatus;
import kz.tastamat.db.model.params.SearchParams;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface PaymentDao {

    <T> PaymentDto insert(Supplier<T> supplier);

    Optional<PaymentDto> findById(Long id);
    Optional<PaymentDto> findByIdentificator(String identificator);
    PaymentDto create(Long userId, PaymentDto dto);
    List<PaymentDto> find(SearchParams params);

    int pid(Long id, String pid);
    int approve(String identificator);
    PaymentDto status(Long id, PaymentStatus status);

    PaymentDto initialize(Long userId, PaymentDto dto);

    PaymentDto epayInitialize(Long userId, PaymentDto dto);

    int succeeded(Long id);

    List<PaymentDto> findByStatus(PaymentStatus status);

    PaymentDto epayApprove(Long id, String ref, String code);

    Optional<PaymentDto> findByPid(String pid);
}
