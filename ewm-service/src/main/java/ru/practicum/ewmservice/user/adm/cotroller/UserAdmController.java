package ru.practicum.ewmservice.user.adm.cotroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.user.adm.service.UserAdmServiceImpl;
import ru.practicum.ewmservice.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Validated
public class UserAdmController {
    private final UserAdmServiceImpl userService;

    @GetMapping
    public List<UserDto> getAll(@RequestParam List<Integer> ids,
                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                @RequestParam(defaultValue = "10") @PositiveOrZero Integer size) {

        return userService.findByIds(ids, from, size);
    }

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto userDto) {

        return userService.create(userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Integer userId) {

        userService.deleteById(userId);
    }
}
