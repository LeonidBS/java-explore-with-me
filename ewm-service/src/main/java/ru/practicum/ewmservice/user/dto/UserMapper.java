package ru.practicum.ewmservice.user.dto;

import ru.practicum.ewmservice.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        if (user != null) {
            return new UserDto(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    0
            );
        } else {
            return null;
        }
    }

    public static List<UserDto> mapListToUserDto(List<User> users) {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users) {
            usersDto.add(mapToUserDto(user));
        }
        return usersDto;
    }

    public static UserShortDto mapToUserShortDto(User user) {
        if (user != null) {
            return new UserShortDto(
                    user.getId(),
                    user.getName(),
                    0
            );
        } else {
            return null;
        }
    }

    public static UserShortForPublicDto mapToUserShortForPublicDto(User user) {
        if (user != null) {
            return new UserShortForPublicDto(
                    user.getId(),
                    user.getName()
            );
        } else {
            return null;
        }
    }


}
