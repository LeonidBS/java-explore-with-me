package ru.practicum.ewmservice.event.pblc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.category.dto.CategoryMapper;
import ru.practicum.ewmservice.category.repository.CategoryRepository;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventMapper;
import ru.practicum.ewmservice.event.dto.EventShortDto;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.IdNotFoundException;
import ru.practicum.ewmservice.exception.MyValidationException;
import ru.practicum.ewmservice.participation.model.ParticipationRequestStatus;
import ru.practicum.ewmservice.participation.repository.ParticipationRepository;
import ru.practicum.statsclient.client.StatsClient;
import ru.practicum.statsdto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    private final CategoryMapper categoryMapper;

    @Override
    public List<EventShortDto> findByFilters(String text, List<Integer> categories, Boolean paid,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                             Boolean onlyAvailable, String sort, Integer from,
                                             Integer size, String ip, String uri) {
        PageRequest page = PageRequest.of(from / size, size);

        if (categories == null) {
            categories = List.copyOf(categoryRepository.findAllIds());
        }

        List<Boolean> paidList;
        if (paid == null) {
            paidList = List.of(false, true);
        } else {
            paidList = List.of(paid);
        }

        log.debug("Save stats {}", statsAdd(ip, uri));
        List<Event> events;

        if (rangeStart == null && rangeEnd == null) {
            if (onlyAvailable) {
                events = eventRepository.findByFiltersFromNow(text, categories,
                        paidList, LocalDateTime.now(), page).toList();

                if (sort.equals("VIEWS")) {
                    return createShortDtoList(events).stream()
                            .filter(dto -> dto.getConfirmedRequests() >= events.get(dto.getId()).getParticipantLimit())
                            .sorted(Comparator.comparing(EventShortDto::getViews))
                            .collect(Collectors.toList());
                } else {
                    return createShortDtoList(events).stream()
                            .filter(dto -> dto.getConfirmedRequests() >= events.get(dto.getId()).getParticipantLimit())
                            .collect(Collectors.toList());
                }

            } else {
                events = eventRepository.findByFiltersFromNow(text, categories,
                        paidList, LocalDateTime.now(), page).toList();

                if (sort.equals("VIEWS")) {
                    return createShortDtoList(events).stream()
                            .sorted(Comparator.comparing(EventShortDto::getViews))
                            .collect(Collectors.toList());
                } else {
                    return createShortDtoList(events);
                }

            }

        } else {

            if (!rangeEnd.isAfter(rangeStart)) {
                throw new MyValidationException("Start must be before End");
            }

            if (onlyAvailable) {
                events = eventRepository.findByFiltersInDateRange(text, categories,
                        paidList, rangeStart, rangeEnd, page).toList();
                if (sort.equals("VIEWS")) {
                    return createShortDtoList(events).stream()
                            .filter(dto -> dto.getConfirmedRequests() >= events.get(dto.getId()).getParticipantLimit())
                            .sorted(Comparator.comparing(EventShortDto::getViews))
                            .collect(Collectors.toList());
                } else {
                    return createShortDtoList(events).stream()
                            .filter(dto -> dto.getConfirmedRequests() >= events.get(dto.getId()).getParticipantLimit())
                            .collect(Collectors.toList());
                }
            } else {
                events = eventRepository.findByFiltersInDateRange(text, categories,
                        paidList, rangeStart, rangeEnd, page).toList();
                if (sort.equals("VIEWS")) {
                    return createShortDtoList(events).stream()
                            .sorted(Comparator.comparing(EventShortDto::getViews))
                            .collect(Collectors.toList());
                } else {
                    return createShortDtoList(events);
                }
            }
        }
    }

    @Override
    public EventFullDto findById(Integer id, String ip, String uri) {

        Event existedEvent = eventRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Event with id={} was not found", id);
                    return new IdNotFoundException(String.format("Event with id=%d was not found", id));
                });

        if (!existedEvent.getState().equals(State.PUBLISHED)) {
            throw new IdNotFoundException(String.format("Event with id=%d was not found", id));
        }

        log.debug(statsAdd(ip, uri));
        log.debug("Request has been added to Stats,");
        return createFullDto(existedEvent);
    }

    private String statsAdd(String ip, String uri) {

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("ewm-service")
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build();

        return statsClient.addStats(endpointHitDto).toString();
    }

    private Map<Integer, Integer> statsGet(List<String> uris) {

        return statsClient.findStatsMap(LocalDateTime.parse("2000-01-05T00:00:00"),
                LocalDateTime.parse("2050-01-05T00:00:00"), uris, true);
    }

    private List<EventShortDto> createShortDtoList(List<Event> events) {
        List<Integer> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        List<Integer> participationCounts = participationRepository.findParticipationCountByEventIdsStatus(eventIds,
                ParticipationRequestStatus.CONFIRMED);
        List<EventShortDto> dtos = eventMapper.mapListToShortDto(events);
        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());
        Map<Integer, Integer> statsMap = statsGet(uris);

        for (int i = 0; i < dtos.size(); i++) {
            dtos.get(i).setCategory(categoryMapper.mapToDto(events.get(i).getCategory()));
            if (!participationCounts.isEmpty()) {
                dtos.get(i).setConfirmedRequests(participationCounts.get(i));

                if (!statsMap.isEmpty()) {
                    dtos.get(i).setViews(statsMap.get(dtos.get(i).getId()));
                }

            }
        }

        return dtos;
    }

    private EventFullDto createFullDto(Event event) {
        Integer participationCounts = participationRepository.findParticipationCountByEventIdAndStatus(event.getId(),
                ParticipationRequestStatus.CONFIRMED);
        EventFullDto dto = eventMapper.mapToDto(event);
        dto.setCategory(categoryMapper.mapToDto(event.getCategory()));
        Map<Integer, Integer> statsMap = statsGet(List.of("/events/" + event.getId()));

        if (!statsMap.isEmpty()) {
            dto.setViews(statsMap.get(event.getId()));
        }

        if (participationCounts != 0) {
            dto.setConfirmedRequests(participationCounts);
        }

        return dto;
    }
}
