package ru.practicum.ewmservice.participation.prvt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.participation.dto.ParticipationDto;
import ru.practicum.ewmservice.participation.prvt.service.ParticipationPrivateService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class ParticipationPrivateController {
    private final ParticipationPrivateService participationPrivateService;

    @GetMapping("/{userId}/requests")
    public List<ParticipationDto> getByRequesterId(@PositiveOrZero @PathVariable Integer userId) {

        return participationPrivateService.findByRequesterId(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationDto create(@PositiveOrZero @PathVariable Integer userId,
                                   @PositiveOrZero @RequestParam Integer eventId) {

        return participationPrivateService.saveParticipation(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationDto update(@PathVariable Integer userId,
                                   @PathVariable Integer requestId) {

        return participationPrivateService.update(userId, requestId);
    }

}
