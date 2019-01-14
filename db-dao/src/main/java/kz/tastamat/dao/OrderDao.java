package kz.tastamat.dao;

import kz.tastamat.db.model.dto.OrderDto;
import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.db.model.enums.Role;
import kz.tastamat.db.model.params.SearchParams;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    Optional<OrderDto> findById(Long id);
//    List<OrderDto> find(SearchParams params);
    OrderDto create(OrderDto dto);
    int reserved(String dropCode, String pickCode, Long id);
    int drop(String dropCode, String pickCode, Long id);
    int sent(Long id);
    int end(Long id);
    int sms(Long id);
}
