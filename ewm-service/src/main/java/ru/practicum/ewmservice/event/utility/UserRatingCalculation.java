package ru.practicum.ewmservice.event.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventShortDto;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.participation.repository.ParticipationRepository;
import ru.practicum.ewmservice.rating.model.Emoji;
import ru.practicum.ewmservice.rating.repository.RatingRepository;
import ru.practicum.ewmservice.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserRatingCalculation {
    private final EventRepository eventRepository;
    private final RatingRepository ratingRepository;
    private final GetEventDto getEventDto;
    private final ParticipationRepository participationRepository;


    public EventFullDto addUserRatingInEventDto(EventFullDto dto) {
        List<Integer> passedEventIds = eventRepository
                .findIdsByInitiatorStateConfirmedInPast(dto.getInitiator().getId(), LocalDateTime.now());
        Map<Integer, Integer> eventLikeCount = ratingRepository.findLikesByInitiatorIdIn(passedEventIds);
        Map<Integer, Integer> eventDislikeCount = ratingRepository.findDislikesByInitiatorIdIn(passedEventIds);
        Map<Integer, Integer> eventParticipantCount = eventRepository.findParticipantLimitsByIdIn(passedEventIds);
        Map<Integer, Integer> eventParticipantLimitCount = participationRepository
                .findMapParticipationCountByEventIdsStatus(passedEventIds);

        int sumRating = 0;
        for (Integer eventId : passedEventIds) {
            sumRating = sumRating + (eventParticipantCount.get(eventId) - eventDislikeCount.get(eventId))
                    * eventLikeCount.get(eventId)
                    / eventParticipantLimitCount.get(eventId);
        }

        dto.getInitiator().setRating(sumRating);

        return dto;
    }

    public EventShortDto getUserRatingForShortDto(EventShortDto dto) {
        List<Integer> passedEventIds = eventRepository
                .findIdsByInitiatorStateConfirmedInPast(dto.getInitiator().getId(), LocalDateTime.now());
        Map<Integer, Integer> eventLikeCount = ratingRepository.findLikesByInitiatorIdIn(passedEventIds);
        Map<Integer, Integer> eventDislikeCount = ratingRepository.findDislikesByInitiatorIdIn(passedEventIds);
        Map<Integer, Integer> eventParticipantCount = eventRepository.findParticipantLimitsByIdIn(passedEventIds);
        Map<Integer, Integer> eventParticipantLimitCount = participationRepository
                .findMapParticipationCountByEventIdsStatus(passedEventIds);

        int sumRating = 0;
        for (Integer eventId : passedEventIds) {
            sumRating = sumRating + (eventParticipantCount.get(eventId) - eventDislikeCount.get(eventId))
                    * eventLikeCount.get(eventId)
                    / eventParticipantLimitCount.get(eventId);
        }

        dto.getInitiator().setRating(sumRating);

        return dto;
    }

    public List<EventFullDto> addUserRatingInEventDtoList(List<EventFullDto> dtos) {
        List<Integer> dtoIds = dtos.stream()
                .map(EventFullDto::getId)
                .collect(Collectors.toList());
        List<Integer> initiatorIds = eventRepository.findDistinctInitiatorIdByIdIn(dtoIds);
        Map<Integer, Integer> passedEventIds = eventRepository
                .findIdsByInitiatorInStateConfirmedInPast(dtoIds, LocalDateTime.now());
        List<Integer>  passedEventIdsList = new ArrayList<>(passedEventIds.keySet());
        Map<Integer, Integer> eventLikeCount = ratingRepository.findLikesByInitiatorIdIn(passedEventIdsList);
        Map<Integer, Integer> eventDislikeCount = ratingRepository.findDislikesByInitiatorIdIn(passedEventIdsList);
        Map<Integer, Integer> eventParticipantCount = eventRepository.findParticipantLimitsByIdIn(passedEventIdsList);
        Map<Integer, Integer> eventParticipantLimitCount = participationRepository
                .findMapParticipationCountByEventIdsStatus(passedEventIdsList);

        for (Integer initiatorId : initiatorIds) {

            List<Integer> eventIdsByInitiator = passedEventIds.entrySet().stream()
                    .filter(map -> map.getValue().equals(initiatorId))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            int sumRating = 0;
            for (Integer eventId : eventIdsByInitiator) {
                sumRating = sumRating + (eventParticipantCount.get(eventId) - eventDislikeCount.get(eventId))
                        * eventLikeCount.get(eventId)
                        / eventParticipantLimitCount.get(eventId);
            }

            for (EventFullDto dto : dtos) {
                if (dto.getInitiator().getId().equals(initiatorId)) {
                    dto.getInitiator().setRating(sumRating);
                }
            }
        }
        return dtos;
    }

    public List<EventShortDto> getUserRatingListFroShortDto(List<EventShortDto> dtos) {
        List<Integer> dtoIds = dtos.stream()
                .map(EventShortDto::getId)
                .collect(Collectors.toList());
        List<Integer> initiatorIds = eventRepository.findDistinctInitiatorIdByIdIn(dtoIds);
        Map<Integer, Integer> passedEventIds = eventRepository
                .findIdsByInitiatorInStateConfirmedInPast(dtoIds, LocalDateTime.now());
        List<Integer>  passedEventIdsList = new ArrayList<>(passedEventIds.keySet());
        Map<Integer, Integer> eventLikeCount = ratingRepository.findLikesByInitiatorIdIn(passedEventIdsList);
        Map<Integer, Integer> eventDislikeCount = ratingRepository.findDislikesByInitiatorIdIn(passedEventIdsList);
        Map<Integer, Integer> eventParticipantCount = eventRepository.findParticipantLimitsByIdIn(passedEventIdsList);
        Map<Integer, Integer> eventParticipantLimitCount = participationRepository
                .findMapParticipationCountByEventIdsStatus(passedEventIdsList);

        for (Integer initiatorId : initiatorIds) {

            List<Integer> eventIdsByInitiator = passedEventIds.entrySet().stream()
                    .filter(map -> map.getValue().equals(initiatorId))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            int sumRating = 0;
            for (Integer eventId : eventIdsByInitiator) {
                sumRating = sumRating + (eventParticipantCount.get(eventId) - eventDislikeCount.get(eventId))
                        * eventLikeCount.get(eventId)
                        / eventParticipantLimitCount.get(eventId);
            }

            for (EventShortDto dto : dtos) {
                if (dto.getInitiator().getId().equals(initiatorId)) {
                    dto.getInitiator().setRating(sumRating);
                }
            }
        }
        return dtos;
    }

    public UserDto addUserRating(UserDto dto) {
        List<Integer> passedEventIds = eventRepository
                .findIdsByInitiatorStateConfirmedInPast(dto.getId(), LocalDateTime.now());
        List<Event> passedEvents = eventRepository
                .findByInitiatorStateConfirmedInPast(dto.getId(), LocalDateTime.now());
        List<Map<Emoji, Integer>> rateMapList = ratingRepository.findMapByEventIds(passedEventIds);
        List<EventFullDto> passedEventDtos = getEventDto.createFullDtoList(passedEvents);

        int sumRating = 0;
        for (int i = 0; i < passedEventIds.size(); i++) {
            sumRating = sumRating + (passedEventDtos.get(i).getConfirmedRequests()
                    - rateMapList.get(i).get(Emoji.DISLIKE))
                    * rateMapList.get(i).get(Emoji.LIKE)
                    / passedEventDtos.get(i).getParticipantLimit();
        }

        dto.setRating(sumRating);

        return dto;
    }

    public List<UserDto> addUserRatingList(List<UserDto> userDtos) {

        List<Integer> initiatorIds = userDtos.stream()
                .map(UserDto::getId)
                .collect(Collectors.toList());
        List<Integer> dtoIds = eventRepository
                .findIdsByInitiatorInStateConfirmedInPastList(initiatorIds, LocalDateTime.now());
        Map<Integer, Integer> passedEventIds = eventRepository
                .findIdsByInitiatorInStateConfirmedInPast(dtoIds, LocalDateTime.now());
        List<Integer>  passedEventIdsList = new ArrayList<>(passedEventIds.keySet());
        Map<Integer, Integer> eventLikeCount = ratingRepository.findLikesByInitiatorIdIn(passedEventIdsList);
        Map<Integer, Integer> eventDislikeCount = ratingRepository.findDislikesByInitiatorIdIn(passedEventIdsList);
        Map<Integer, Integer> eventParticipantCount = eventRepository.findParticipantLimitsByIdIn(passedEventIdsList);
        Map<Integer, Integer> eventParticipantLimitCount = participationRepository
                .findMapParticipationCountByEventIdsStatus(passedEventIdsList);

        for (Integer initiatorId : initiatorIds) {

            List<Integer> eventIdsByInitiator = passedEventIds.entrySet().stream()
                    .filter(map -> map.getValue().equals(initiatorId))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            int sumRating = 0;
            for (Integer eventId : eventIdsByInitiator) {
                sumRating = sumRating + (eventParticipantCount.get(eventId) - eventDislikeCount.get(eventId))
                        * eventLikeCount.get(eventId)
                        / eventParticipantLimitCount.get(eventId);
            }

            for (UserDto dto : userDtos) {
                if (dto.getId().equals(initiatorId)) {
                    dto.setRating(sumRating);
                }
            }
        }
        return userDtos;
    }
}
