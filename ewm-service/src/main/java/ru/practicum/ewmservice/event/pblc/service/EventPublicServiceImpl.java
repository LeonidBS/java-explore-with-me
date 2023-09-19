package ru.practicum.ewmservice.event.pblc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventMapper;
import ru.practicum.ewmservice.event.dto.EventShortDto;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.IdNotFoundException;
import ru.practicum.ewmservice.exception.MyValidationException;
import ru.practicum.ewmservice.participation.model.ParticipationRequestStatus;
import ru.practicum.ewmservice.participation.repository.ParticipationRepository;
import ru.practicum.statsclient.client.StatsClient;
import ru.practicum.statsdto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPublicServiceImpl implements EventPublicService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;
    private final ParticipationRepository participationRepository;

    @Override
    public List<EventShortDto> findByFilters(String text, List<Integer> categories, Boolean paid,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                             Boolean onlyAvailable, String sort, Integer from,
                                             Integer size, HttpServletRequest httpServletRequest) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        if (rangeStart == null && rangeEnd == null) {

            if (onlyAvailable) {
                    log.debug("Request has been added to Stats," + statsRequest(httpServletRequest).toString());
                return eventRepository.findByFiltersFromNow(text, categories,
                                paid, LocalDateTime.now(), sort, page).stream()
                        .peek(event -> eventRepository.updateViewByEventId(event.getId()))
                        .filter(event ->
                            participationRepository.findParticipationCountByEventIdAndStatus(event.getId(),
                                    ParticipationRequestStatus.CONFIRMED) >= event.getParticipantLimit()
                        )
                        .map(eventMapper::mapToShortDto)
                        .collect(Collectors.toList());
            } else {
                log.debug("Request has been added to Stats," + statsRequest(httpServletRequest).toString());
                return eventRepository.findByFiltersFromNow(text, categories,
                                paid, LocalDateTime.now(), sort, page).stream()
                        .peek(event -> eventRepository.updateViewByEventId(event.getId()))
                        .map(eventMapper::mapToShortDto)
                        .collect(Collectors.toList());
            }

        } else {

            if (!rangeEnd.isAfter(rangeStart)) {
                log.error("Start must be before End");
                throw new MyValidationException("Start must be before End");
            }

            if (onlyAvailable) {
                log.debug("Request has been added to Stats," + statsRequest(httpServletRequest).toString());
                return eventRepository.findByFiltersInDateRange(text, categories,
                                paid, rangeStart, rangeEnd,sort, page).stream()
                        .peek(event -> eventRepository.updateViewByEventId(event.getId()))
                        .filter(event ->
                                participationRepository.findParticipationCountByEventIdAndStatus(event.getId(),
                                        ParticipationRequestStatus.CONFIRMED) >= event.getParticipantLimit()
                        )
                        .map(eventMapper::mapToShortDto)
                        .collect(Collectors.toList());
            } else {
                log.debug("Request has been added to Stats," + statsRequest(httpServletRequest).toString());
                return eventRepository.findByFiltersInDateRange(text, categories,
                                paid, rangeStart, rangeEnd, sort, page).stream()
                        .peek(event -> eventRepository.updateViewByEventId(event.getId()))
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
                    return new IdNotFoundException(String.format("Event with id=%d was not found"
                            , id));
                });

        log.debug("Request has been added to Stats," + statsRequest(httpServletRequest).toString());
        eventRepository.updateViewByEventId(id);

        return eventMapper.mapToDto(existedEvent);
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
}
