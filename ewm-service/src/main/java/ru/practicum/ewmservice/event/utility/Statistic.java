package ru.practicum.ewmservice.event.utility;

import ru.practicum.statsclient.client.StatsClient;
import ru.practicum.statsdto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Statistic {

    public static String statsAdd(String ip, String uri, StatsClient statsClient) {

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("ewm-service")
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build();

        return statsClient.addStats(endpointHitDto).toString();
    }

    public static Map<Integer, Integer> statsGet(List<String> uris, StatsClient statsClient) {

        return statsClient.findStatsMap(LocalDateTime.parse("2000-01-05T00:00:00"),
                LocalDateTime.parse("2050-01-05T00:00:00"), uris, true);
    }
}
