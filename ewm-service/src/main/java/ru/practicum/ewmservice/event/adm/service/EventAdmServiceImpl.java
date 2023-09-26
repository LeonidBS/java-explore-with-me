package ru.practicum.ewmservice.event.adm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.category.dto.CategoryMapper;
import ru.practicum.ewmservice.category.repository.CategoryRepository;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventMapper;
import ru.practicum.ewmservice.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;
import ru.practicum.ewmservice.event.model.StateAdminAction;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.event.repository.LocationRepository;
import ru.practicum.ewmservice.exception.EventValidationException;
import ru.practicum.ewmservice.exception.IdNotFoundException;
import ru.practicum.ewmservice.exception.MyValidationException;
import ru.practicum.ewmservice.participation.model.ParticipationRequestStatus;
import ru.practicum.ewmservice.participation.repository.ParticipationRepository;
import ru.practicum.statsclient.client.StatsClient;
import ru.practicum.statsdto.ViewStatsDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventAdmServiceImpl implements EventAdmService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final ParticipationRepository participationRepository;
    private final CategoryMapper categoryMapper;
    private final StatsClient statsClient;

    @Override
    public List<EventFullDto> findEventsByAdmin(List<Integer> users, List<State> states,
                                                List<Integer> categories, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);

        if (rangeStart != null && rangeEnd != null) {
            if (!rangeEnd.isAfter(rangeStart)) {
                throw new MyValidationException("Start must be before End");
            }
        }

        if (states == null) {
            states = List.of(State.PENDING, State.PUBLISHED, State.CANCELED, State.REJECTED);
        }

        List<Event> events;

        if (users != null) {
            if (categories != null) {

                if (rangeStart != null && rangeEnd != null) {
                    events = eventRepository.findByInitiatorStateCategoryEventDate(
                            users, states, categories, rangeStart, rangeEnd, page).toList();
                } else {
                    events = eventRepository.findByInitiatorStateCategory(
                            users, states, categories, page).toList();
                }

            } else {

                if (rangeStart != null && rangeEnd != null) {
                    events = eventRepository.findByInitiatorStateEventDate(
                            users, states, rangeStart, rangeEnd, page).toList();
                } else {
                    events = eventRepository
                            .findByInitiatorIdInAndStateInOrderByEventDateDescIdAsc(
                                    users, states, page).toList();
                }

            }
        } else {
            if (categories != null) {

                if (rangeStart != null && rangeEnd != null) {
                    events = eventRepository
                            .findByStateCategoryEventDate(
                                    states, categories, rangeStart, rangeEnd, page).toList();
                } else {
                    events = eventRepository
                            .findByStateInAndCategoryIdInOrderByEventDateDescIdAsc(
                                    states, categories, page).toList();
                }

            } else {

                if (rangeStart != null && rangeEnd != null) {
                    events = eventRepository
                            .findByStateEventDate(
                                    states, rangeStart, rangeEnd, page).toList();
                } else {

                    events = eventRepository
                            .findByStateInOrderByEventDateDescIdAsc(
                                    states, page).toList();
                }

            }
        }

        List<Integer> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        List<Integer> participationCounts = participationRepository
                .findParticipationCountByEventIdsStatus(eventIds,ParticipationRequestStatus.CONFIRMED);
        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());
        List<EventFullDto> dtos = eventMapper.mapListToDto(events);
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

    @Override
    public EventFullDto update(UpdateEventAdminRequest updateEventAdminRequest, Integer eventId) {

        Event event = eventRepository.findByIdFetch(eventId)
                .orElseThrow(() -> {
                    log.error("Event with ID {} has not been found", eventId);
                    return new IdNotFoundException("There is no Event with ID: " + eventId);
                });

        if (updateEventAdminRequest.getEventDate() == null) {
            if (Duration.between(event.getEventDate(), LocalDateTime.now())
                    .compareTo(Duration.parse("PT1H")) >= 0) {
                log.error("EventDate {} after CreateOn {} less than 60 minutes",
                        updateEventAdminRequest.getEventDate(), event.getCreatedOn());
                throw new EventValidationException("EventDate after CreateOn less than 60 minutes");
            }
        }

        if (!event.getState().equals(State.PENDING)
                && updateEventAdminRequest.getStateAction().equals(StateAdminAction.PUBLISH_EVENT)) {
            log.error("Event is not at PENDING state, it cannot be PUBLISHED");
            throw new EventValidationException("Event is not at PENDING state, it cannot be PUBLISHED");
        }

        if (event.getState().equals(State.PUBLISHED)
                && updateEventAdminRequest.getStateAction().equals(StateAdminAction.REJECT_EVENT)) {
            log.error("Event is at PUBLISH state, it cannot be REJECTED");
            throw new EventValidationException("Event is at PUBLISH state, it cannot be REJECTED");
        }

        if (updateEventAdminRequest.getCategory() != null) {
            if (categoryRepository.findById(updateEventAdminRequest.getCategory()).isPresent()) {
                event.setCategory(categoryRepository.findById(updateEventAdminRequest.getCategory()).get());
            }
        }

        if (updateEventAdminRequest.getAnnotation() != null) event.setAnnotation(
                updateEventAdminRequest.getAnnotation());
        if (updateEventAdminRequest.getDescription() != null) event.setDescription(
                updateEventAdminRequest.getDescription());
        if (updateEventAdminRequest.getEventDate() != null) event.setEventDate(
                updateEventAdminRequest.getEventDate());

        if (updateEventAdminRequest.getLocation() != null) {
            if (updateEventAdminRequest.getLocation().getId() == null) {
                event.setLocation(locationRepository.save(updateEventAdminRequest.getLocation()));
            } else {
                event.setLocation(updateEventAdminRequest.getLocation());
            }
        }

        if (updateEventAdminRequest.getPaid() != null) event.setPaid(
                updateEventAdminRequest.getPaid());
        if (updateEventAdminRequest.getParticipantLimit() != null) event.setParticipantLimit(
                updateEventAdminRequest.getParticipantLimit());
        if (updateEventAdminRequest.getRequestModeration() != null) event.setRequestModeration(
                updateEventAdminRequest.getRequestModeration());
        if (updateEventAdminRequest.getTitle() != null) event.setTitle(
                updateEventAdminRequest.getTitle());

        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(StateAdminAction.PUBLISH_EVENT)) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (updateEventAdminRequest.getStateAction().equals(StateAdminAction.REJECT_EVENT)) {
                event.setState(State.REJECTED);
            }
        }

        log.debug("Event has been updated: {}", event);

        Integer participationCounts = participationRepository.findParticipationCountByEventIdAndStatus(event.getId(),
                ParticipationRequestStatus.CONFIRMED);
        EventFullDto dto = eventMapper.mapToDto(event);
        dto.setCategory(categoryMapper.mapToDto(event.getCategory()));
        dto.setConfirmedRequests(participationCounts);
        Map<Integer, Integer> statsMap = statsGet(List.of("/events/" + event.getId()));

        if (!statsMap.isEmpty()) {
            dto.setViews(statsMap.get(event.getId()));
        }

        return dto;
    }

    private Map<Integer, Integer> statsGet(List<String> uris) {
        List<ViewStatsDto> response = statsClient.findStats(LocalDateTime.parse("2000-01-05T00:00:00"),
                LocalDateTime.parse("2050-01-05T00:00:00"), uris, true);
        Map<Integer, Integer> statsMap = new HashMap<>();

        if (!response.isEmpty()) {
            for (ViewStatsDto viewStatsDto : response) {
                statsMap.put(Integer.parseInt(viewStatsDto.getUri().substring(8)), viewStatsDto.getHits());
            }
        }
        return statsMap;
    }
}
