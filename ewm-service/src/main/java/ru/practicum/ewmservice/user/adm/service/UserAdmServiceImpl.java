package ru.practicum.ewmservice.user.adm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.user.dto.NewUserRequest;
import ru.practicum.ewmservice.user.dto.UserDto;
import ru.practicum.ewmservice.user.dto.UserMapper;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;
import ru.practicum.ewmservice.exception.IdNotFoundException;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserAdmServiceImpl implements UserAdmService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    @Override
    public List<UserDto> findByIds(List<Integer> ids, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        if (ids != null) {
            return UserMapper.mapListToUserDto(userRepository.findByIdIn(ids, page).toList());
        } else {
            return UserMapper.mapListToUserDto(userRepository.findAll(page).toList());
        }
    }

    @Override
    @Transactional
    public UserDto create(NewUserRequest newUserRequest) {

        User user = User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();

        log.debug("User has been created: {}", user);
        return UserMapper.mapToUserDto(userRepository.save(user));
    }


    @Override
    @Transactional
    public void deleteById(Integer id) {

        if (userRepository.findById(id).isEmpty()) {
            log.error("User with id={} was not found", id);
            throw new IdNotFoundException(String.format("User with id=%d was not found", id));
        }

        if (!eventRepository.findByInitiatorIdOrderByEventDateDescIdAsc(id).isEmpty()) {
            log.error("User has created Event, it cannot be deleted");
            throw new IdNotFoundException("User has created Event, it cannot be deleted");
        }

        userRepository.deleteById(id);
    }
}
