package ru.practicum.ewmservice.event.pblc.service;

import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {
    List<EventShortDto> findByFilters(String text, List<Integer> categories, Boolean paid,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                      Boolean onlyAvailable, String sort, Integer from,
                                      Integer size, HttpServletRequest httpServletRequest);

    EventFullDto findById(Integer id, HttpServletRequest httpServletRequest);
}
