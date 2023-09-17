package ru.practicum.ewmservice.event.adm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.event.adm.service.EventAdmService;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.event.model.State;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
public class EventAdmController {
    private final EventAdmService eventAdmService;

    @GetMapping
    public List<EventFullDto> getEventsForByAdmin(@RequestParam List<Integer> users,
                                                  @RequestParam List<State> states,
                                                  @RequestParam List<Integer> categories,
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                  @RequestParam LocalDateTime rangeStart,
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                  @RequestParam LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10") @PositiveOrZero Integer size) {

        return eventAdmService.findEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest,
                               @PathVariable Integer eventId) {

        return eventAdmService.update(updateEventAdminRequest, eventId);
    }

}

