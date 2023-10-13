package ru.practicum.ewmservice.user.adm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.event.utility.UserRatingService;
import ru.practicum.ewmservice.exception.IdNotFoundException;
import ru.practicum.ewmservice.user.dto.NewUserRequest;
import ru.practicum.ewmservice.user.dto.UserDto;
import ru.practicum.ewmservice.user.dto.UserMapper;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserAdmServiceImpl implements UserAdmService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final UserRatingService userRatingService;


    @Override
    public List<UserDto> findByIds(List<Integer> ids, int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);

        if (ids != null) {
            return userRatingService.addUserRatingList(
                    UserMapper.mapListToUserDto(userRepository.findByIdIn(ids, page).toList()));
        } else {
            return userRatingService.addUserRatingList(
                    UserMapper.mapListToUserDto(userRepository.findAll(page).toList()));
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

        if (!userRepository.existsById(id)) {
            throw new IdNotFoundException(String.format("User with id=%d was not found", id));
        }

        if (!eventRepository.findByInitiatorIdOrderByEventDateDescIdAsc(id).isEmpty()) {
            throw new IdNotFoundException("User has created Event, it cannot be deleted");
        }

        userRepository.deleteById(id);
    }
}
