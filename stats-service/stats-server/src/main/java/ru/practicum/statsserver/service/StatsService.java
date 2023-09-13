package ru.practicum.statsserver.service;

import ru.practicum.statsdto.EndpointHitDto;
import ru.practicum.statsdto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris,
                                 boolean unique, Integer from, Integer size);

    EndpointHitDto addStats(EndpointHitDto endpointHitDto);
}
