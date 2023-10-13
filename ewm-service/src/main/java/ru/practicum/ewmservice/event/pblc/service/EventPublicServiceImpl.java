package ru.practicum.ewmservice.event.pblc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.category.repository.CategoryRepository;
import ru.practicum.ewmservice.event.dto.EventFullPublicDto;
import ru.practicum.ewmservice.event.dto.EventShortPublicDto;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.event.utility.GetEventDto;
import ru.practicum.ewmservice.event.utility.Statistic;
import ru.practicum.ewmservice.exception.IdNotFoundException;
import ru.practicum.ewmservice.exception.MyValidationException;
import ru.practicum.statsclient.client.StatsClient;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventPublicServiceImpl implements EventPublicService {
    private final EventRepository eventRepository;
    private final StatsClient statsClient;
    private final CategoryRepository categoryRepository;
    private final GetEventDto getEventDto;

    @Override
    public List<EventShortPublicDto> findByFilters(String text, List<Integer> categories, Boolean paid,
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

        log.debug("Save stats {}", Statistic.statsAdd(ip, uri, statsClient));
        List<Event> events;

        if (rangeStart == null && rangeEnd == null) {
            if (onlyAvailable) {
                events = eventRepository.findByFiltersFromNow(text, categories,
                        paidList, LocalDateTime.now(), page).toList();

          return sortIfAvailableOnly(sort, events);

            } else {
                events = eventRepository.findByFiltersFromNow(text, categories,
                        paidList, LocalDateTime.now(), page).toList();

                return sortIfAll(sort, events);
            }

        } else {

            if (!rangeEnd.isAfter(rangeStart)) {
                throw new MyValidationException("Start must be before End");
            }

            if (onlyAvailable) {
                events = eventRepository.findByFiltersInDateRange(text, categories,
                        paidList, rangeStart, rangeEnd, page).toList();

                return sortIfAvailableOnly(sort, events);
            } else {
                events = eventRepository.findByFiltersInDateRange(text, categories,
                        paidList, rangeStart, rangeEnd, page).toList();
                return sortIfAll(sort, events);
            }
        }
    }

    @Override
    public EventFullPublicDto findById(Integer id, String ip, String uri) {

        Event existedEvent = eventRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(String.format("Event with id=%d was not found", id)));

        if (!existedEvent.getState().equals(State.PUBLISHED)) {
            throw new IdNotFoundException(String.format("Event with id=%d was not found", id));
        }

        log.debug(Statistic.statsAdd(ip, uri, statsClient));
        log.debug("Request has been added to Stats,");
        return getEventDto.createFullPublicDto(existedEvent);
    }

    private List<EventShortPublicDto> sortIfAvailableOnly(String sort, List<Event> events) {
        switch (sort) {
            case "VIEWS":
                return getEventDto.createShortPublicDtoList(events).stream()
                        .filter(dto -> dto.getConfirmedRequests() >= events.get(dto.getId()).getParticipantLimit())
                        .sorted(Comparator.comparing(EventShortPublicDto::getViews))
                        .collect(Collectors.toList());
            case "EVENT_DATE":
                return getEventDto.createShortPublicDtoList(events).stream()
                        .filter(dto -> dto.getConfirmedRequests() >= events.get(dto.getId()).getParticipantLimit())
                        .sorted(Comparator.comparing(EventShortPublicDto::getEventDate))
                        .collect(Collectors.toList());
            case "RATING":
                return getEventDto.createShortPublicDtoList(events).stream()
                        .filter(dto -> dto.getConfirmedRequests() >= events.get(dto.getId()).getParticipantLimit())
                        .sorted(Comparator.comparing(EventShortPublicDto::getRating))
                        .collect(Collectors.toList());
            default:
                return getEventDto.createShortPublicDtoList(events).stream()
                        .filter(dto -> dto.getConfirmedRequests() >= events.get(dto.getId()).getParticipantLimit())
                        .collect(Collectors.toList());
        }
    }

    private List<EventShortPublicDto> sortIfAll(String sort, List<Event> events) {
        switch (sort) {
            case "VIEWS":
                return getEventDto.createShortPublicDtoList(events).stream()
                        .sorted(Comparator.comparing(EventShortPublicDto::getViews))
                        .collect(Collectors.toList());
            case "EVENT_DATE":
                return getEventDto.createShortPublicDtoList(events).stream()
                        .sorted(Comparator.comparing(EventShortPublicDto::getEventDate))
                        .collect(Collectors.toList());
            case "RATING":
                return getEventDto.createShortPublicDtoList(events).stream()
                        .sorted(Comparator.comparing(EventShortPublicDto::getRating))
                        .collect(Collectors.toList());
            default:
                return getEventDto.createShortPublicDtoList(events);
        }
    }
}
