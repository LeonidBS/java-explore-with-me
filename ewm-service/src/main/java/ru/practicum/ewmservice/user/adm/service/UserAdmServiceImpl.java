package ru.practicum.ewmservice.user.adm.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.user.dto.UserDto;

import java.util.List;

@Transactional(readOnly = true)
public interface UserAdmServiceImpl {
    List<UserDto> findByIds(List<Integer> ids, int from, int size);

    UserDto create(UserDto userDto);

    void deleteById(Integer id);
}
