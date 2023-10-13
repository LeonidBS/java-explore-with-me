package ru.practicum.ewmservice.event.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventShortDto;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.rating.repository.RatingRepository;
import ru.practicum.ewmservice.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserRatingService {
    private final EventRepository eventRepository;
    private final RatingRepository ratingRepository;

    public Integer addUserRatingInEventDto(EventFullDto dto) {
        List<Integer> passedEventIds = eventRepository
                .findIdsByInitiatorStateConfirmedInPast(dto.getInitiator().getId());
        Map<Integer, Long> eventLikeCount = ratingRepository.findLikesByInitiatorIdIn(passedEventIds);
        Map<Integer, Long> eventDislikeCount = ratingRepository.findDislikesByInitiatorIdIn(passedEventIds);
        Map<Integer, Integer> eventParticipantCount = eventRepository.findParticipantLimitsByIdIn(passedEventIds);

        int sumRating = 0;
        for (Integer eventId : passedEventIds) {
            sumRating = sumRating + (eventParticipantCount.getOrDefault(eventId, 0)
                    - eventDislikeCount.getOrDefault(eventId, 0L).intValue())
                    * eventLikeCount.getOrDefault(eventId, 0L).intValue();
        }

        return sumRating;
    }

    public List<EventFullDto> addUserRatingInEventDtoList(List<EventFullDto> dtos) {
        List<Integer> dtoIds = dtos.stream()
                .map(EventFullDto::getId)
                .collect(Collectors.toList());
        List<Integer> initiatorIds = eventRepository.findDistinctInitiatorIdByIdIn(dtoIds);
        Map<Integer, Integer> passedEventIds = eventRepository
                .findIdsByInitiatorInStateConfirmedInPast(dtoIds);
        List<Integer> passedEventIdsList = new ArrayList<>(passedEventIds.keySet());
        Map<Integer, Long> eventLikeCount = ratingRepository.findLikesByInitiatorIdIn(passedEventIdsList);
        Map<Integer, Long> eventDislikeCount = ratingRepository.findDislikesByInitiatorIdIn(passedEventIdsList);
        Map<Integer, Integer> eventParticipantCount = eventRepository.findParticipantLimitsByIdIn(passedEventIdsList);

        for (Integer initiatorId : initiatorIds) {

            List<Integer> eventIdsByInitiator = passedEventIds.entrySet().stream()
                    .filter(map -> map.getValue().equals(initiatorId))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            int sumRating = 0;
            for (Integer eventId : eventIdsByInitiator) {
                sumRating = sumRating + (eventParticipantCount.getOrDefault(eventId, 0)
                        - eventDislikeCount.getOrDefault(eventId, 0L).intValue())
                        * eventLikeCount.getOrDefault(eventId, 0L).intValue();
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
                .findIdsByInitiatorInStateConfirmedInPast(dtoIds);
        List<Integer> passedEventIdsList = new ArrayList<>(passedEventIds.keySet());
        Map<Integer, Long> eventLikeCount = ratingRepository.findLikesByInitiatorIdIn(passedEventIdsList);
        Map<Integer, Long> eventDislikeCount = ratingRepository.findDislikesByInitiatorIdIn(passedEventIdsList);
        Map<Integer, Integer> eventParticipantCount = eventRepository.findParticipantLimitsByIdIn(passedEventIdsList);

        for (Integer initiatorId : initiatorIds) {

            List<Integer> eventIdsByInitiator = passedEventIds.entrySet().stream()
                    .filter(map -> map.getValue().equals(initiatorId))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            int sumRating = 0;
            for (Integer eventId : eventIdsByInitiator) {
                sumRating = sumRating + (eventParticipantCount.getOrDefault(eventId, 0)
                        - eventDislikeCount.getOrDefault(eventId, 0L).intValue())
                        * eventLikeCount.getOrDefault(eventId, 0L).intValue();
            }

            for (EventShortDto dto : dtos) {
                if (dto.getInitiator().getId().equals(initiatorId)) {
                    dto.getInitiator().setRating(sumRating);
                }
            }
        }
        return dtos;
    }

    public List<UserDto> addUserRatingList(List<UserDto> userDtos) {

        List<Integer> initiatorIds = userDtos.stream()
                .map(UserDto::getId)
                .collect(Collectors.toList());
        List<Integer> dtoIds = eventRepository
                .findIdsByInitiatorInStateConfirmedInPastList(initiatorIds);
        Map<Integer, Integer> passedEventIds = eventRepository
                .findIdsByInitiatorInStateConfirmedInPast(dtoIds);
        List<Integer> passedEventIdsList = new ArrayList<>(passedEventIds.keySet());
        Map<Integer, Long> eventLikeCount = ratingRepository.findLikesByInitiatorIdIn(passedEventIdsList);
        Map<Integer, Long> eventDislikeCount = ratingRepository.findDislikesByInitiatorIdIn(passedEventIdsList);
        Map<Integer, Integer> eventParticipantCount = eventRepository.findParticipantLimitsByIdIn(passedEventIdsList);

        for (Integer initiatorId : initiatorIds) {

            List<Integer> eventIdsByInitiator = passedEventIds.entrySet().stream()
                    .filter(map -> map.getValue().equals(initiatorId))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            int sumRating = 0;
            for (Integer eventId : eventIdsByInitiator) {
                sumRating = sumRating + (eventParticipantCount.getOrDefault(eventId, 0)
                        - eventDislikeCount.getOrDefault(eventId, 0L).intValue())
                        * eventLikeCount.getOrDefault(eventId, 0L).intValue();
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
