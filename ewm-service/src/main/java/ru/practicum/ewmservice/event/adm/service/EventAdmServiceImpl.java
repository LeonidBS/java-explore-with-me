package ru.practicum.ewmservice.event.adm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventMapper;
import ru.practicum.ewmservice.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;
import ru.practicum.ewmservice.event.model.StateAction;
import ru.practicum.ewmservice.exception.EventValidationException;
import ru.practicum.ewmservice.exception.IdNotFoundException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventAdmServiceImpl implements EventAdmService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;


    @Override
    public List<EventFullDto> findEventsByAdmin(List<Integer> users, List<State> states,
                                                List<Integer> categories, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        return eventMapper.mapListToDto(eventRepository
                .findByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetweenOrderByEventDate(
                        users, states, categories, rangeStart, rangeEnd, page));
    }

    @Override
    public EventFullDto update(UpdateEventAdminRequest updateEventAdminRequest, Integer eventId) {

        Event existedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.error("Event with ID {} has not been found", eventId);
                    return new IdNotFoundException("There is no Event with ID: " + eventId);
                });

        if (ChronoUnit.MINUTES.between(existedEvent.getCreatedOn(),
                updateEventAdminRequest.getEventDate()) < 60) {
            log.error("EventDate {} after CreateOn {} less than 60 minutes",
                    updateEventAdminRequest.getEventDate(), existedEvent.getCreatedOn());
            throw new EventValidationException("EventDate after CreateOn less than 60 minutes");
        }

        if (existedEvent.getState() != State.PENDING
                && updateEventAdminRequest.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            log.error("Event is not at PENDING state");
            throw new EventValidationException("Event is not at PUBLISHED state");
        }

        if (existedEvent.getState() == State.PENDING
                && updateEventAdminRequest.getStateAction().equals(StateAction.REJECT_EVENT)) {
            log.error("Event is rejected");
            throw new EventValidationException("Event is rejected");
        }

        Event event = Event.builder()
                .annotation(updateEventAdminRequest.getAnnotation())
                .category(updateEventAdminRequest.getCategory())
                .description(updateEventAdminRequest.getDescription())
                .eventDate(updateEventAdminRequest.getEventDate())
                .location(updateEventAdminRequest.getLocation())
                .paid(updateEventAdminRequest.getPaid())
                .participantLimit(updateEventAdminRequest.getParticipantLimit())
                .requestModeration(updateEventAdminRequest.getRequestModeration())
                .title(updateEventAdminRequest.getTitle())
                .build();

        log.debug("Event has been updated: {}", event);
        return eventMapper.mapToDto(eventRepository.save(event));
    }
}
