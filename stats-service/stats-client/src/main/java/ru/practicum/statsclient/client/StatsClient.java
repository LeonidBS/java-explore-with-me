package ru.practicum.statsclient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statsdto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsClient {

    @Value("${stats-server.url}")
    private final String url;
    protected RestTemplate rest = new RestTemplate();

    public ResponseEntity<Object> findStats(LocalDateTime start, LocalDateTime end,
                                            List<String> uris, Boolean unique) throws JsonProcessingException {
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
            return get(url + "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                    parameters);
        } else {
            parameters = Map.of(
                    "start", DateTimeFormatter.ofPattern(datetimePattern).format(start),
                    "end", DateTimeFormatter.ofPattern(datetimePattern).format(end),
                    "unique", unique
            );
            return get(url + "/stats?start={start}&end={end}&unique={unique}",
                    parameters);
        }
    }

    public ResponseEntity<Object> addStats(EndpointHitDto endpointHitDto) {
        return post(url + "/hit", endpointHitDto);
    }

    protected ResponseEntity<Object> get(String path, @Nullable Map<String, Object> parameters) {
        ResponseEntity<Object> statsServerResponse;

        try {
            assert parameters != null;
            statsServerResponse = rest.exchange(path, HttpMethod.GET, null, Object.class, parameters);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }

        return prepareGatewayResponse(statsServerResponse);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        ResponseEntity<Object> statsServerResponse;

        try {
            statsServerResponse = rest.exchange(path, HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }

        return prepareGatewayResponse(statsServerResponse);
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
