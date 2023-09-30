package ru.practicum.ewmservice.event.prvt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.event.dto.*;
import ru.practicum.ewmservice.event.prvt.service.EventPrivateService;
import ru.practicum.ewmservice.participation.dto.ParticipationDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class EventPrivateController {
    private final EventPrivateService eventPrivateService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getByInitiatorId(@PositiveOrZero @PathVariable Integer userId,
                                                @RequestParam(required = false) String sort,
                                                @PositiveOrZero(message
                                                        = "page should be positive or 0")
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @Positive(message
                                                        = "size should be positive number")
                                                @RequestParam(defaultValue = "10") Integer size) {

        return eventPrivateService.findByInitiatorId(userId, from, size, sort);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@Valid @RequestBody NewEventDto newEventDto,
                               @PositiveOrZero @PathVariable Integer userId) {

        return eventPrivateService.create(newEventDto, userId);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getByIdAndInitiatorId(@PositiveOrZero @PathVariable Integer userId,
                                              @PositiveOrZero @PathVariable Integer eventId) {

        return eventPrivateService.findByIdAndInitiatorId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto patchEvent(@PositiveOrZero @PathVariable Integer userId,
                                   @PositiveOrZero @PathVariable Integer eventId,
                                   @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {

        return eventPrivateService.updateEvent(updateEventUserRequest, userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationDto> getByEventIdAndEventInitiatorId(@PositiveOrZero @PathVariable Integer userId,
                                                                  @PositiveOrZero @PathVariable Integer eventId) {

        return eventPrivateService.findByEventIdAndEventInitiatorId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateParticipation(@RequestBody @Valid EventRequestStatusUpdateRequest
                                                                      eventRequestStatusUpdateRequest,
                                                              @PositiveOrZero @PathVariable Integer userId,
                                                              @PositiveOrZero @PathVariable Integer eventId) {

        return eventPrivateService.updateParticipation(eventRequestStatusUpdateRequest, userId, eventId);
    }
}
