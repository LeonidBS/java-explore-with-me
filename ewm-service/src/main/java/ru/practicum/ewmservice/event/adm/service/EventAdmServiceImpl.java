package ru.practicum.ewmservice.event.adm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventAdmServiceImpl implements EventAdmService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<EventFullDto> findEventsByAdmin(List<Integer> users, List<State> states,
                                                List<Integer> categories, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        if (rangeStart != null && rangeEnd != null) {
            if (!rangeEnd.isAfter(rangeStart)) {
                log.error("Start must be before End");
                throw new MyValidationException("Start must be before End");
            }
        }

        if (states == null) {
            states = List.of(State.PENDING, State.PUBLISHED, State.CANCELED, State.REJECTED);
        }

        List<Event> events = new ArrayList<>();

        if (users != null) {
            if (categories != null) {
                if (rangeStart != null && rangeEnd != null) {

                    for (Event event : eventRepository
                            .findByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetweenOrderByEventDateDescIdAsc(
                                    users, states, categories, rangeStart, rangeEnd, page).toList()) {
                        event.setViews(event.getViews() + 1);
                        events.add(eventRepository.save(event));
                    }

                } else {

                    for (Event event : eventRepository
                            .findByInitiatorIdInAndStateInAndCategoryIdInOrderByEventDateDescIdAsc(
                                    users, states, categories, page).toList()) {
                        event.setViews(event.getViews() + 1);
                        events.add(eventRepository.save(event));
                    }

                }
            } else {
                if (rangeStart != null && rangeEnd != null) {

                    for (Event event : eventRepository
                            .findByInitiatorIdInAndStateInAndEventDateBetweenOrderByEventDateDescIdAsc(
                                    users, states, rangeStart, rangeEnd, page).toList()) {
                        event.setViews(event.getViews() + 1);
                        events.add(eventRepository.save(event));
                    }

                } else {

                    for (Event event : eventRepository
                            .findByInitiatorIdInAndStateInOrderByEventDateDescIdAsc(
                                    users, states, page).toList()) {
                        event.setViews(event.getViews() + 1);
                        events.add(eventRepository.save(event));
                    }

                }
            }
        } else {

            if (categories != null) {
                if (rangeStart != null && rangeEnd != null) {

                    for (Event event : eventRepository
                            .findByStateInAndCategoryIdInAndEventDateBetweenOrderByEventDateDescIdAsc(
                                    states, categories, rangeStart, rangeEnd, page)) {
                        event.setViews(event.getViews() + 1);
                        events.add(eventRepository.save(event));
                    }

                } else {

                    for (Event event : eventRepository
                            .findByStateInAndCategoryIdInOrderByEventDateDescIdAsc(
                                    states, categories, page)) {
                        event.setViews(event.getViews() + 1);
                        events.add(eventRepository.save(event));
                    }

                }

            } else {
                if (rangeStart != null && rangeEnd != null) {

                    for (Event event : eventRepository
                            .findByStateInAndEventDateBetweenOrderByEventDateDescIdAsc(
                                    states, rangeStart, rangeEnd, page).toList()) {
                        event.setViews(event.getViews() + 1);
                        events.add(eventRepository.save(event));
                    }

                } else {

                    for (Event event : eventRepository
                            .findByStateInOrderByEventDateDescIdAsc(
                                    states, page).toList()) {
                        event.setViews(event.getViews() + 1);
                        events.add(eventRepository.save(event));
                    }

                }
            }
        }

        return eventMapper.mapListToDto(events);
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
        return eventMapper.mapToDto(eventRepository.save(event));
    }
}
