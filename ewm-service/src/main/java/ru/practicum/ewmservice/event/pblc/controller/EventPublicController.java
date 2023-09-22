package ru.practicum.ewmservice.event.pblc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventShortDto;
import ru.practicum.ewmservice.event.pblc.service.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
public class EventPublicController {
    private final EventPublicService eventPublicService;

    @GetMapping
    public List<EventShortDto> getByFilters(@RequestParam(defaultValue = "") String text,
                                            @RequestParam(required = false) List<Integer> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                            @RequestParam(required = false) LocalDateTime rangeStart,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                            @RequestParam(required = false) LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(defaultValue = "10") @PositiveOrZero Integer size,
                                            HttpServletRequest httpServletRequest) {

        return eventPublicService.findByFilters(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, httpServletRequest);
    }

    @GetMapping("/{id}")
    public EventFullDto getById(@PositiveOrZero @PathVariable Integer id,
                                HttpServletRequest httpServletRequest) {

        return eventPublicService.findById(id, httpServletRequest);
    }
}
