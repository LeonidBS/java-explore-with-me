package ru.practicum.ewmservice.user.adm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.event.adm.repository.EventAdmRepository;
import ru.practicum.ewmservice.exception.IdNotFoundException;
import ru.practicum.ewmservice.user.adm.repository.UserAdmRepository;
import ru.practicum.ewmservice.user.dto.UserDto;
import ru.practicum.ewmservice.user.dto.UserMapper;
import ru.practicum.ewmservice.user.model.User;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserAdmService implements UserAdmServiceImpl {
    private final UserAdmRepository userRepository;
    private final EventAdmRepository eventAdmRepository;


    @Override
    public List<UserDto> findByIds(List<Integer> ids, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        return UserMapper.mapListToUserDto(userRepository.findByIdIn(ids, page).toList());
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();

        log.debug("User has been created: {}", user);
        return UserMapper.mapToUserDto(userRepository.save(user));
    }


    @Override
    @Transactional
    public void deleteById(Integer id) {

        if (userRepository.findById(id).isEmpty()) {
            log.error("User with ID {}} has not been found", id);
            throw new IdNotFoundException(String.format("User with ID %d has not been found", id));
        }

        if (!eventAdmRepository.findByInitiatorId(id).isEmpty()) {
            log.error("User with ID {}} has not been found", id);
            throw new IdNotFoundException(String.format("User with ID %d has not been found", id));
        }

        userRepository.deleteByUserId(id);
    }
}
