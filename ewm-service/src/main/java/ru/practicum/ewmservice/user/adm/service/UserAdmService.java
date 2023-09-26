package ru.practicum.ewmservice.user.adm.service;

import ru.practicum.ewmservice.user.dto.NewUserRequest;
import ru.practicum.ewmservice.user.dto.UserDto;

import java.util.List;

public interface UserAdmService {
    List<UserDto> findByIds(List<Integer> ids, int from, int size);

    UserDto create(NewUserRequest newUserRequest);

    void deleteById(Integer id);
}
