package ru.practicum.ewmservice.participation.prvt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.participation.dto.ParticipationDto;
import ru.practicum.ewmservice.participation.dto.ParticipationMapper;
import ru.practicum.ewmservice.participation.prvt.service.ParticipationPrivateService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/{userId}/request")
@RequiredArgsConstructor
@Validated
public class ParticipationPrivateController {
    ParticipationPrivateService participationPrivateService;
    ParticipationMapper participationMapper;

    @GetMapping
    public List<ParticipationDto> getByRequesterId(@PositiveOrZero @PathVariable Integer userId) {

        return participationPrivateService.findByRequesterId(userId);
    }

    @PostMapping
    public ParticipationDto create(@PositiveOrZero @PathVariable Integer userId,
                                 @PositiveOrZero @RequestParam Integer eventId) {

        return participationPrivateService.create(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationDto update(@PathVariable Integer userId,
                                 @PathVariable Integer requestId) {

        return participationPrivateService.update(userId, requestId);
    }

}
