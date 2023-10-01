package ru.practicum.ewmservice.event.prvt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.category.dto.CategoryMapper;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.category.repository.CategoryRepository;
import ru.practicum.ewmservice.event.dto.*;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.Location;
import ru.practicum.ewmservice.event.model.State;
import ru.practicum.ewmservice.event.model.StateAction;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.event.repository.LocationRepository;
import ru.practicum.ewmservice.event.utility.GetEventDto;
import ru.practicum.ewmservice.event.utility.UserRatingCalculation;
import ru.practicum.ewmservice.exception.AccessDeniedException;
import ru.practicum.ewmservice.exception.IdNotFoundException;
import ru.practicum.ewmservice.exception.MyValidationException;
import ru.practicum.ewmservice.participation.dto.ParticipationDto;
import ru.practicum.ewmservice.participation.dto.ParticipationMapper;
import ru.practicum.ewmservice.participation.model.Participation;
import ru.practicum.ewmservice.participation.model.ParticipationRequestStatus;
import ru.practicum.ewmservice.participation.repository.ParticipationRepository;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventPrivateServiceImpl implements EventPrivateService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationMapper participationMapper;
    private final LocationRepository locationRepository;
    private final CategoryMapper categoryMapper;
    private final GetEventDto getEventDto;
    private final ParticipationRepository participationRepository;
    private final UserRatingCalculation userRatingCalculation;

    @Override
    public List<EventShortDto> findByInitiatorId(Integer userId, Integer from, Integer size, String sort) {
        PageRequest page = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findByInitiatorIdOrderByEventDateDescIdAsc(userId, page).toList();

        if (sort == null) {
            return userRatingCalculation.getUserRatingListFroShortDto(getEventDto.createShortDtoList(events));
        } else if (sort.equals("EVENT_RATING")) {
            return userRatingCalculation.getUserRatingListFroShortDto(
                            getEventDto.createShortDtoList(events)).stream()
                    .sorted(Comparator.comparing(EventShortDto::getRating))
                    .collect(Collectors.toList());
        } else if (sort.equals("USER_RATING")) {
            return userRatingCalculation.getUserRatingListFroShortDto(
                            getEventDto.createShortDtoList(events)).stream()
                    .sorted(Comparator.comparing(dto -> dto.getInitiator().getRating()))
                    .collect(Collectors.toList());
        } else {
            return userRatingCalculation.getUserRatingListFroShortDto(getEventDto.createShortDtoList(events));
        }
    }

    @Override
    public EventFullDto create(NewEventDto newEventDto, Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFoundException("There is no User with ID: " + userId));

        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new IdNotFoundException("There is no Category with ID: "
                        + newEventDto.getCategory()));

        Location location = null;
        if (newEventDto.getLocation() != null) {
            if (newEventDto.getLocation().getId() == null) {
                location = locationRepository.save(newEventDto.getLocation());
            } else {
                location = newEventDto.getLocation();
            }
        }

        Event event = Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .initiator(user)
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(location)
                .paid(newEventDto.getPaid() != null ?
                        newEventDto.getPaid() : false)
                .participantLimit(newEventDto.getParticipantLimit() != null ?
                        newEventDto.getParticipantLimit() : 0)
                .requestModeration(newEventDto.getRequestModeration() != null ?
                        newEventDto.getRequestModeration() : true)
                .title(newEventDto.getTitle())
                .publishedOn(null)
                .state(State.PENDING)
                .build();

        log.debug("Event has been created: {}", event);

        EventFullDto dto = EventMapper.mapToDto(eventRepository.save(event));
        dto.setViews(0);
        dto.setCategory(categoryMapper.mapToDto(event.getCategory()));
        return dto;
    }

    @Override
    public EventFullDto findByIdAndInitiatorId(Integer userId, Integer eventId) {

        EventFullDto eventFullDto = getEventDto
                .createFullDto(checkEventInitiator(userId, eventId));
       Integer rating = userRatingCalculation.addUserRatingInEventDto(eventFullDto);
        eventFullDto.getInitiator().setRating(rating);
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(UpdateEventUserRequest updateEventUserRequest, Integer userId, Integer eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IdNotFoundException("There is no Event with ID: " + eventId));

        if (Duration.between(event.getEventDate(), LocalDateTime.now())
                .compareTo(Duration.parse("PT2H")) >= 0) {
            throw new AccessDeniedException("Event Date must be after for 2 hours");
        }

        if (event.getState().equals(State.PUBLISHED)) {
            throw new AccessDeniedException("Only pending or canceled events can be changed");
        }

        if (!event.getInitiator().getId().equals(userId)) {
            throw new MyValidationException(String.format("Initiator ID of Event with ID %d is not equal userId %d",
                    eventId, userId));
        }

        if (updateEventUserRequest.getCategory() != null) {
            if (categoryRepository.findById(updateEventUserRequest.getCategory()).isPresent()) {
                event.setCategory(categoryRepository.findById(updateEventUserRequest.getCategory()).get());
            }
        }

        if (updateEventUserRequest.getAnnotation() != null) event.setAnnotation(
                updateEventUserRequest.getAnnotation());
        if (updateEventUserRequest.getDescription() != null) event.setDescription(
                updateEventUserRequest.getDescription());
        if (updateEventUserRequest.getEventDate() != null) event.setEventDate(
                updateEventUserRequest.getEventDate());

        if (updateEventUserRequest.getLocation() != null) {
            if (updateEventUserRequest.getLocation().getId() == null) {
                event.setLocation(locationRepository.save(updateEventUserRequest.getLocation()));
            } else {
                event.setLocation(updateEventUserRequest.getLocation());
            }
        }

        if (updateEventUserRequest.getPaid() != null) event.setPaid(
                updateEventUserRequest.getPaid());
        if (updateEventUserRequest.getParticipantLimit() != null) event.setParticipantLimit(
                updateEventUserRequest.getParticipantLimit());
        if (updateEventUserRequest.getRequestModeration() != null) event.setRequestModeration(
                updateEventUserRequest.getRequestModeration());
        if (updateEventUserRequest.getTitle() != null) event.setTitle(
                updateEventUserRequest.getTitle());

        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            } else if (updateEventUserRequest.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            }
        }

        Event evenewEvent = eventRepository.save(event);
        EventFullDto eventFullDto = getEventDto.createFullDto(evenewEvent);
        int rating =  userRatingCalculation.addUserRatingInEventDto(eventFullDto);
        eventFullDto.getInitiator().setRating(rating);
        return eventFullDto;
    }

    @Override
    public List<ParticipationDto> findByEventIdAndEventInitiatorId(Integer userId, Integer eventId) {
        checkEventInitiator(userId, eventId);

        return participationMapper.mapListToDto(participationRepository.findByEventId(eventId));
    }

    @Override
    public EventRequestStatusUpdateResult updateParticipation(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                              Integer userId, Integer eventId) {
        Event event = checkEventInitiator(userId, eventId);
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult;
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            eventRequestStatusUpdateResult = EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(participationMapper.mapListToDto(
                            participationRepository.findByEventId(eventId)))
                    .rejectedRequests(null)
                    .build();
            return eventRequestStatusUpdateResult;
        }

        if (participationRepository.findParticipationCountByEventId(eventId) >= event.getParticipantLimit()) {
            log.error("The participant limit has been reached");
            throw new AccessDeniedException("The participant limit has been reached");
        }

        List<ParticipationDto> confirmedRequests = new ArrayList<>();
        List<ParticipationDto> rejectedRequests = new ArrayList<>();

        for (Participation participation : participationRepository
                .findByIdInAndEventId(eventRequestStatusUpdateRequest.getRequestIds(), eventId)) {
            if (participation.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                if (eventRequestStatusUpdateRequest.getStatus()
                        .equals(ParticipationRequestStatus.CONFIRMED)) {
                    participation.setStatus(ParticipationRequestStatus.CONFIRMED);
                    confirmedRequests.add(participationMapper.mapToDto(participation));
                }
                if (eventRequestStatusUpdateRequest.getStatus()
                        .equals(ParticipationRequestStatus.REJECTED)) {
                    participation.setStatus(ParticipationRequestStatus.REJECTED);
                    rejectedRequests.add(participationMapper.mapToDto(participation));
                }
                participationRepository.updateParticipationStatusById(eventRequestStatusUpdateRequest.getStatus(),
                        participation.getId());
            }
        }

        if (confirmedRequests.isEmpty() && rejectedRequests.isEmpty()) {
            throw new AccessDeniedException("Participation status is not PENDING");
        }

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    private Event checkEventInitiator(Integer userId, Integer eventId) {

        Event event = eventRepository.findByIdFetch(eventId)
                .orElseThrow(() -> new IdNotFoundException("There is no Event with ID: " + eventId));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new MyValidationException(String.format("Initiator ID of Event with ID %d is not equal userId %d",
                    eventId, userId));
        }

        return event;
    }
}
