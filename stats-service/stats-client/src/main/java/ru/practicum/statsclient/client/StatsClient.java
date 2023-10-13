package ru.practicum.statsclient.client;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statsclient.exception.ResponseEntityErrorException;
import ru.practicum.statsdto.EndpointHitDto;
import ru.practicum.statsdto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class StatsClient {

    @Value("${stats-server.url}")
    private String url;

    private RestTemplate rest = new RestTemplate();

    public Map<Integer, Integer> findStatsMap(LocalDateTime start, LocalDateTime end,
                                              List<String> uris, Boolean unique) {

        Map<String, Object> parameters;
        String datetimePattern = "yyyy-MM-dd HH:mm:ss";
        StringBuilder urisString = new StringBuilder();
        if (uris != null) {
            int i = 0;
            for (String s : uris) {
                urisString.append(s);
                if (++i < uris.size()) {
                    urisString.append(",");
                }
            }
            parameters = Map.of(
                    "start", DateTimeFormatter.ofPattern(datetimePattern).format(start),
                    "end", DateTimeFormatter.ofPattern(datetimePattern).format(end),
                    "uris", urisString.toString(),
                    "unique", unique
            );
            ResponseEntity<Object> response = get(url
                            + "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                    parameters);
            if (response.getStatusCodeValue() == 200) {
                Map<Integer, Integer> statsMap = new HashMap<>();

                for (ViewStatsDto viewStatsDto : (ViewStatsDto[]) Objects.requireNonNull(response.getBody())) {
                        if (viewStatsDto.getUri().length() >= 8) {
                            statsMap.put(Integer.parseInt(viewStatsDto.getUri().substring(8)),
                                    viewStatsDto.getHits());
                        } else {
                            statsMap.put(0,0);
                        }
                }

                return statsMap;

            } else if (response.getStatusCode().is4xxClientError()) {
                throw new ResponseEntityErrorException("Bad request");
            } else {
                throw new ResponseEntityErrorException("Request is not correct");
            }

        } else {
            parameters = Map.of(
                    "start", DateTimeFormatter.ofPattern(datetimePattern).format(start),
                    "end", DateTimeFormatter.ofPattern(datetimePattern).format(end),
                    "unique", unique
            );
            ResponseEntity<Object> response = get(url
                            + "/stats?start={start}&end={end}&unique={unique}",
                    parameters);

            if (response.getStatusCodeValue() == 200) {
                Map<Integer, Integer> statsMap = new HashMap<>();
                for (ViewStatsDto viewStatsDto : (ViewStatsDto[]) Objects.requireNonNull(response.getBody())) {
                    statsMap.put(Integer.parseInt(viewStatsDto.getUri().substring(8)),
                            viewStatsDto.getHits());
                }

                return statsMap;

            } else if (response.getStatusCode().is4xxClientError()) {
                throw new ResponseEntityErrorException("Bad request");
            } else {
                throw new ResponseEntityErrorException("Request is not correct");
            }

        }
    }

    public List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end,
                                        List<String> uris, Boolean unique) {

        Map<String, Object> parameters;
        String datetimePattern = "yyyy-MM-dd HH:mm:ss";
        StringBuilder urisString = new StringBuilder();
        if (uris != null) {
            int i = 0;
            for (String s : uris) {
                urisString.append(s);
                if (++i < uris.size()) {
                    urisString.append(",");
                }
            }
            parameters = Map.of(
                    "start", DateTimeFormatter.ofPattern(datetimePattern).format(start),
                    "end", DateTimeFormatter.ofPattern(datetimePattern).format(end),
                    "uris", urisString.toString(),
                    "unique", unique
            );
            ResponseEntity<Object> response = get(url
                            + "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                    parameters);
            if (response.getStatusCodeValue() == 200) {
                return Arrays.asList((ViewStatsDto[]) Objects.requireNonNull(response.getBody()));
            } else if (response.getStatusCode().is4xxClientError()) {
                throw new ResponseEntityErrorException("Bad request");
            } else {
                throw new ResponseEntityErrorException("Request is not correct");
            }

        } else {
            parameters = Map.of(
                    "start", DateTimeFormatter.ofPattern(datetimePattern).format(start),
                    "end", DateTimeFormatter.ofPattern(datetimePattern).format(end),
                    "unique", unique
            );
            ResponseEntity<Object> response = get(url
                            + "/stats?start={start}&end={end}&unique={unique}",
                    parameters);

            if (response.getStatusCodeValue() == 200) {
                return Arrays.asList((ViewStatsDto[]) Objects.requireNonNull(response.getBody()));
            } else if (response.getStatusCode().is4xxClientError()) {
                throw new ResponseEntityErrorException("Bad request");
            } else {
                throw new ResponseEntityErrorException("Request is not correct");
            }

        }
    }

    public Integer findUrl(String ip, String uri) {
        Map<String, Object> parameters;
        parameters = Map.of(
                "ip", ip,
                "uri", uri
        );
        ResponseEntity<Object> response = getString(url + "/stats/url?ip={ip}&uri={uri}", parameters);

        if (response.getStatusCodeValue() == 200) {
            return (Integer) Objects.requireNonNull(response.getBody());
        } else if (response.getStatusCode().is4xxClientError()) {
            throw new ResponseEntityErrorException("Bad request");
        } else {
            throw new ResponseEntityErrorException("Request is not correct");
        }
    }

    public EndpointHitDto addStats(EndpointHitDto endpointHitDto) {

        return (EndpointHitDto) post(url + "/hit", endpointHitDto).getBody();
    }

    protected ResponseEntity<Object> get(String path, @Nullable Map<String, Object> parameters) {
        try {
            assert parameters != null;
            ResponseEntity<?> responseEntity = rest.exchange(path, HttpMethod.GET,
                    null, (Class<?>) ViewStatsDto[].class, parameters);
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    protected ResponseEntity<Object> getString(String path, @Nullable Map<String, Object> parameters) {
        try {
            assert parameters != null;
            ResponseEntity<?> responseEntity = rest.exchange(path, HttpMethod.GET,
                    null, (Class<?>) Integer.class, parameters);
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);

        try {
            ResponseEntity<T> responseEntity = rest.exchange(path, HttpMethod.POST,
                    requestEntity, (Class<T>) EndpointHitDto.class);
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }
}