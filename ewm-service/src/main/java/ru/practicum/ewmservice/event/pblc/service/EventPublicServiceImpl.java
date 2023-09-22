package ru.practicum.ewmservice.event.pblc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.category.repository.CategoryRepository;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventMapper;
import ru.practicum.ewmservice.event.dto.EventShortDto;
import ru.practicum.ewmservice.exception.IdNotFoundException;
import ru.practicum.ewmservice.exception.MyValidationException;
import ru.practicum.ewmservice.participation.model.ParticipationRequestStatus;
import ru.practicum.ewmservice.participation.repository.ParticipationRepository;
import ru.practicum.statsclient.client.StatsClient;
import ru.practicum.statsdto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventPublicServiceImpl implements EventPublicService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;
    private final ParticipationRepository participationRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EventShortDto> findByFilters(String text, List<Integer> categories, Boolean paid,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                             Boolean onlyAvailable, String sort, Integer from,
                                             Integer size, HttpServletRequest httpServletRequest) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        if (categories == null) {
            categories = List.copyOf(categoryRepository.findAllIds());
        }

        List<Boolean> paidList;
        if (paid == null) {
            paidList = List.of(false, true);
        } else {
            paidList = List.of(paid);
        }


        Boolean uniqueFlag = statsGet(httpServletRequest);
        if (uniqueFlag) log.debug("Request has been added to Stats," +
                statsRequest(httpServletRequest).toString());

        List<Event> events = new ArrayList<>();

        if (rangeStart == null && rangeEnd == null) {
            if (onlyAvailable) {

                for (Event event : eventRepository.findByFiltersFromNow(text, categories,
                        paidList, LocalDateTime.now(), sort, page).toList()) {
                    events.add(event);
                    eventRepository.updateViewByEventId(event.getId());
                }

                return events.stream()
                        .filter(event ->
                                participationRepository.findParticipationCountByEventIdAndStatus(event.getId(),
                                        ParticipationRequestStatus.CONFIRMED) >= event.getParticipantLimit()
                        )
                        .map(eventMapper::mapToShortDto)
                        .collect(Collectors.toList());
            } else {

                for (Event event : eventRepository.findByFiltersFromNow(text, categories,
                        paidList, LocalDateTime.now(), sort, page).toList()) {
                    events.add(event);
                    eventRepository.updateViewByEventId(event.getId());
                }

                return events.stream()
                        .map(eventMapper::mapToShortDto)
                        .collect(Collectors.toList());
            }

        } else {

            if (!rangeEnd.isAfter(rangeStart)) {
                log.error("Start must be before End");
                throw new MyValidationException("Start must be before End");
            }

            if (onlyAvailable) {

                for (Event event : eventRepository.findByFiltersInDateRange(text, categories,
                        paidList, rangeStart, rangeEnd, sort, page).toList()) {
                    events.add(event);
                    eventRepository.updateViewByEventId(event.getId());
                }

                return events.stream()
                        .filter(event ->
                                participationRepository.findParticipationCountByEventIdAndStatus(event.getId(),
                                        ParticipationRequestStatus.CONFIRMED) >= event.getParticipantLimit()
                        )
                        .map(eventMapper::mapToShortDto)
                        .collect(Collectors.toList());
            } else {

                for (Event event : eventRepository.findByFiltersInDateRange(text, categories,
                        paidList, rangeStart, rangeEnd, sort, page).toList()) {
                    events.add(event);
                    eventRepository.updateViewByEventId(event.getId());
                }

                return events.stream()
                        .map(eventMapper::mapToShortDto)
                        .collect(Collectors.toList());
            }
        }
    }

    @Override
    public EventFullDto findById(Integer id, HttpServletRequest httpServletRequest) {

        Event existedEvent = eventRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Event with id={} was not found", id);
                    return new IdNotFoundException(String.format("Event with id=%d was not found", id));
                });

        if (!existedEvent.getState().equals(State.PUBLISHED)) {
            log.error("Event with id={} was not found", id);
            throw new IdNotFoundException(String.format("Event with id=%d was not found", id));
        }

            if (statsGet(httpServletRequest)) {
                log.debug("Request has been added to Stats," + statsRequest(httpServletRequest).toString());
            }
        EventFullDto dto = eventMapper.mapToDto(existedEvent);
            dto.setViews(dto.getViews() + 1);
        return dto;
    }

    private ResponseEntity<Object> statsRequest(HttpServletRequest httpServletRequest) {

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("ewm-service")
                .uri(httpServletRequest.getRequestURI())
                .ip(httpServletRequest.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        return statsClient.addStats(endpointHitDto);
    }

    private Boolean statsGet(HttpServletRequest httpServletRequest) {
        List<String> uris = List.of(httpServletRequest.getRemoteAddr()
                + httpServletRequest.getRequestURI());
        String[] response = statsClient.findStats(LocalDateTime.parse("2000-05-05T00:00:00"),
                        LocalDateTime.parse("2050-05-05T00:00:00"), uris, true).getBody().toString()
                .split("\"hits\": ");

        if (response != null && !response[0].equals("[]")) {
            return false;
        } else {
            return true;

        }
    }
}
