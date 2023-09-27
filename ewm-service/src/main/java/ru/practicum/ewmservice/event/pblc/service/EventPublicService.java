package ru.practicum.ewmservice.event.pblc.service;

import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {
    List<EventShortDto> findByFilters(String text, List<Integer> categories, Boolean paid,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                      Boolean onlyAvailable, String sort, Integer from,
                                      Integer size, String ip, String uri);

    EventFullDto findById(Integer id, String ip, String uri);
}
