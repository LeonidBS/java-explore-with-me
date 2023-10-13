package ru.practicum.ewmservice.event.pblc.service;

import ru.practicum.ewmservice.event.dto.EventFullPublicDto;
import ru.practicum.ewmservice.event.dto.EventShortPublicDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {
    List<EventShortPublicDto> findByFilters(String text, List<Integer> categories, Boolean paid,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                            Boolean onlyAvailable, String sort, Integer from,
                                            Integer size, String ip, String uri);

    EventFullPublicDto findById(Integer id, String ip, String uri);
}
