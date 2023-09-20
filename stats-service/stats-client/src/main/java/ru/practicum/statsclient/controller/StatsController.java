package ru.practicum.statsclient.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsclient.client.StatsClientImpl;
import ru.practicum.statsdto.EndpointHitDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class StatsController {
   private final StatsClientImpl statsClient;

    @GetMapping("/stats")
    public ResponseEntity<Object> findStats(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                            @RequestParam LocalDateTime start,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                            @RequestParam LocalDateTime end,
                                            @RequestParam(required = false) List<String> uris,
                                            @RequestParam(defaultValue = "false") boolean unique)
            throws JsonProcessingException {

        return statsClient.findStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> addStats(@RequestBody  @Valid EndpointHitDto endpointHitDto) {

        return statsClient.addStats(endpointHitDto);
    }

}

