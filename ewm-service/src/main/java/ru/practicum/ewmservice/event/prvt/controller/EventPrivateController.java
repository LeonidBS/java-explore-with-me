package ru.practicum.ewmservice.event.prvt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.event.dto.*;
import ru.practicum.ewmservice.event.prvt.service.EventPrivateService;
import ru.practicum.ewmservice.participation.dto.ParticipationDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class EventPrivateController {
    private final EventPrivateService eventPrivateService;

    @GetMapping
    public List<EventShortDto> getByInitiatorId(@PositiveOrZero @PathVariable Integer userId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @PositiveOrZero Integer size) {

        return eventPrivateService.findByInitiatorId(userId, from, size);
    }

    @PostMapping
    public EventFullDto create(@RequestBody @Valid NewEventDto newEventDto,
                               @PositiveOrZero @PathVariable Integer userId) {

        return eventPrivateService.create(newEventDto, userId);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getByIdAndInitiatorId(@PositiveOrZero @PathVariable Integer userId,
                                              @PositiveOrZero @PathVariable Integer eventId) {

        return eventPrivateService.findByIdAndInitiatorId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@RequestBody @Valid UpdateEventUserRequest updateEventUserRequest,
                                    @PositiveOrZero @PathVariable Integer userId,
                                    @PositiveOrZero @PathVariable Integer eventId) {

        return eventPrivateService.update(updateEventUserRequest, userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationDto> getByEventIdAndEventInitiatorId(@PositiveOrZero @PathVariable Integer userId,
                                                                  @PositiveOrZero @PathVariable Integer eventId) {

        return eventPrivateService.findByEventIdAndEventInitiatorId(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateParticipation(@RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                              @PositiveOrZero @PathVariable Integer userId,
                                                              @PositiveOrZero @PathVariable Integer eventId) {

        return eventPrivateService.updateParticipation(eventRequestStatusUpdateRequest, userId, eventId);
    }
}
