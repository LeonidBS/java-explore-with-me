package ru.practicum.ewmservice.participation.prvt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.IdNotFoundException;
import ru.practicum.ewmservice.exception.ParticipationRequestException;
import ru.practicum.ewmservice.participation.dto.ParticipationDto;
import ru.practicum.ewmservice.participation.dto.ParticipationMapper;
import ru.practicum.ewmservice.participation.model.Participation;
import ru.practicum.ewmservice.participation.model.ParticipationRequestStatus;
import ru.practicum.ewmservice.participation.repository.ParticipationRepository;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationPrivateServiceImpl implements ParticipationPrivateService {

    private final ParticipationRepository participationRepository;
    private final ParticipationMapper participationMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<ParticipationDto> findByRequesterId(Integer userId) {

        return participationMapper.mapListToDto(participationRepository
                .findByRequesterId(userId));
    }

    @Override
    @Transactional
    public ParticipationDto saveParticipation(Integer userId, Integer eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IdNotFoundException("There is no Event with ID: " + eventId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFoundException("There is no User with ID: " + userId));

        if (event.getInitiator().equals(user)) {
            throw new ParticipationRequestException("Event Initiator is equal Participation requester");
        }

        if (!participationRepository.findByRequesterIdAndEventId(userId, eventId).isEmpty()) {
            throw new ParticipationRequestException(String.format(
                    "ParticipationRequest of requester ID %d and Event ID %d is exist", userId, eventId));
        }

        if (eventRepository.findInitiatorIdById(eventId).equals(userId)) {
            throw new ParticipationRequestException(String.format(
                    "Event Initiator ID is equal userId %d", userId));
        }

        if (!eventRepository.findStateById(eventId).equals(State.PUBLISHED)) {
            throw new ParticipationRequestException("Event staus is not PUBLISHED");
        }

        if (eventRepository.findParticipantLimitById(eventId) > 0) {
            if (eventRepository.findParticipantLimitById(eventId)
                    <= participationRepository.findParticipationCountByEventId(eventId)) {
                throw new ParticipationRequestException("ParticipationLimit is reached");
            }
        }

        Participation participation = Participation.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(!eventRepository.findRequestModerationById(eventId)
                        || eventRepository.findParticipantLimitById(eventId) == 0
                        ? ParticipationRequestStatus.CONFIRMED : ParticipationRequestStatus.PENDING)
                .build();

        return participationMapper.mapToDto(participationRepository.save(participation));
    }

    @Override
    @Transactional
    public ParticipationDto update(Integer userId, Integer requestId) {

        Participation existParticipation = participationRepository.findById(requestId)
                .orElseThrow(() -> new IdNotFoundException("There is no Event with ID: " + requestId));

       if (!userRepository.existsById(userId)) {
                    throw new IdNotFoundException("There is no Event with ID: " + userId);
                }

        existParticipation.setStatus(ParticipationRequestStatus.CANCELED);

        return participationMapper.mapToDto(participationRepository.save(existParticipation));
    }
}
