package ru.practicum.statsclient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.statsdto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StatsClient {

    ResponseEntity<Object> findStats(LocalDateTime start, LocalDateTime end,
                                     List<String> uris, Boolean unique)  throws JsonProcessingException;

    ResponseEntity<Object> addStats(EndpointHitDto endpointHitDto);
}
