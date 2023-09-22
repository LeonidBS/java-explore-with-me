package ru.practicum.statsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsdto.EndpointHitDto;
import ru.practicum.statsdto.ViewStatsDto;
import ru.practicum.statsserver.service.StatsServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsController {
    private final StatsServiceImpl statsService;

    @GetMapping("/stats")
    public List<ViewStatsDto> findStats(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                        @RequestParam(required = false) LocalDateTime start,
                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                        @RequestParam(required = false) LocalDateTime end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(defaultValue = "false") boolean unique) {

        return statsService.findStats(start, end, uris, unique, 0, 10);
    }

    @PostMapping("/hit")
    public EndpointHitDto addStats(@RequestBody EndpointHitDto endpointHitDto) {

        return statsService.addStats(endpointHitDto);
    }

}
