package ru.practicum.ewmservice.event.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.category.dto.CategoryMapper;
import ru.practicum.ewmservice.event.dto.*;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.participation.model.ParticipationRequestStatus;
import ru.practicum.ewmservice.participation.repository.ParticipationRepository;
import ru.practicum.ewmservice.rating.model.Emoji;
import ru.practicum.ewmservice.rating.repository.RatingRepository;
import ru.practicum.statsclient.client.StatsClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class GetEventDto {
    private final ParticipationRepository participationRepository;
    private final StatsClient statsClient;
    private final RatingRepository ratingRepository;

    public List<EventShortDto> createShortDtoList(List<Event> events) {
        List<Integer> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        Map<Integer, Long> participationCounts = participationRepository.findParticipationCountByEventIdsStatus(eventIds,
                ParticipationRequestStatus.CONFIRMED);
        List<EventShortDto> dtos = EventMapper.mapListToShortDto(events);
        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());
        Map<Integer, Map<Emoji, Long>> ratesList = ratingRepository.findMapByEventIds(eventIds);
        Map<Integer, Integer> statsMap = Statistic.statsGet(uris, statsClient);
        Integer confirmedRequests = 0;
        for (int i = 0; i < dtos.size(); i++) {
            EventShortDto dto = dtos.get(i);
            dto.setCategory(CategoryMapper.INSTANCE.mapToDto(events.get(i).getCategory()));
            if (!participationCounts.isEmpty()) {
                confirmedRequests = participationCounts.getOrDefault(dto.getId(), 0L).intValue();
                dto.setConfirmedRequests(confirmedRequests);

                if (!statsMap.isEmpty()) {
                    dto.setViews(statsMap.get(dto.getId()));
                }

            }

            if (!ratesList.isEmpty()) {
                Map<Emoji, Long> rates = ratesList.get(dto.getId());
                dto.setRates(rates);
                dto.setRating(ratingCalculation(rates, confirmedRequests));
            }
        }


        return dtos;
    }

    public List<EventFullDto> createFullDtoList(List<Event> events) {
        List<Integer> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        Map<Integer, Long> participationCounts = participationRepository.findParticipationCountByEventIdsStatus(eventIds,
                ParticipationRequestStatus.CONFIRMED);
        List<EventFullDto> dtos = EventMapper.mapListToFullDto(events);
        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());
        Map<Integer, Map<Emoji, Long>> ratesList = ratingRepository.findMapByEventIds(eventIds);
        Map<Integer, Integer> statsMap = Statistic.statsGet(uris, statsClient);
        int confirmedRequests = 0;

        for (int i = 0; i < dtos.size(); i++) {
            EventFullDto dto = dtos.get(i);
            dto.setCategory(CategoryMapper.INSTANCE.mapToDto(events.get(i).getCategory()));
            if (!participationCounts.isEmpty()) {
                confirmedRequests = participationCounts.getOrDefault(dto.getId(), 0L).intValue();
                dto.setConfirmedRequests(confirmedRequests);

                if (!statsMap.isEmpty()) {
                    dto.setViews(statsMap.get(dto.getId()));
                }
            }

            if (!ratesList.isEmpty()) {
                Map<Emoji, Long> rates = ratesList.get(dto.getId());
                dto.setRates(rates);
                dto.setRating(ratingCalculation(rates, confirmedRequests));
            }

        }

        return dtos;
    }

    public EventFullDto createFullDto(Event event) {
        Integer participationCounts = participationRepository
                .findParticipationCountByEventIdAndStatus(event.getId(),
                        ParticipationRequestStatus.CONFIRMED);
        EventFullDto dto = EventMapper.mapToDto(event);
        dto.setCategory(CategoryMapper.INSTANCE.mapToDto(event.getCategory()));
        Map<Integer, Integer> statsMap = Statistic.statsGet(List.of("/events/" + event.getId()),
                statsClient);

        if (!statsMap.isEmpty()) {
            dto.setViews(statsMap.get(event.getId()));
        }

        if (participationCounts != 0) {
            dto.setConfirmedRequests(participationCounts);
        }

        Map<Emoji, Long> rates = ratingRepository.findMapByEventId(event.getId());
        dto.setRates(rates);
        dto.setRating(ratingCalculation(rates, participationCounts));
        return dto;
    }

    public List<EventShortPublicDto> createShortPublicDtoList(List<Event> events) {
        List<Integer> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        Map<Integer, Long> participationCounts = participationRepository.findParticipationCountByEventIdsStatus(eventIds,
                ParticipationRequestStatus.CONFIRMED);
        List<EventShortPublicDto> dtos = EventMapper.mapListToShortPublicDto(events);
        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());
        Map<Integer, Map<Emoji, Long>> ratesList = ratingRepository.findMapByEventIds(eventIds);
        Map<Integer, Integer> statsMap = Statistic.statsGet(uris, statsClient);
        Integer confirmedRequests = 0;

        for (int i = 0; i < dtos.size(); i++) {
            EventShortPublicDto dto = dtos.get(i);
            dto.setCategory(CategoryMapper.INSTANCE.mapToDto(events.get(i).getCategory()));
            if (participationCounts != null) {
                dto.setConfirmedRequests(
                        participationCounts.getOrDefault(dto.getId(), 0L).intValue());

                if (!statsMap.isEmpty()) {
                    dto.setViews(statsMap.getOrDefault(dto.getId(), 0));
                }

            }

            if (!ratesList.isEmpty()) {
                Map<Emoji, Long> rates = ratesList.get(dto.getId());
                if (rates != null) {
                    dto.setRates(rates);
                    dto.setRating(ratingCalculation(rates, confirmedRequests));
                }
            }
        }


        return dtos;
    }

    public EventFullPublicDto createFullPublicDto(Event event) {
        Integer participationCounts = participationRepository
                .findParticipationCountByEventIdAndStatus(event.getId(),
                        ParticipationRequestStatus.CONFIRMED);
        EventFullPublicDto dto = EventMapper.mapToPublicDto(event);
        dto.setCategory(CategoryMapper.INSTANCE.mapToDto(event.getCategory()));
        Map<Integer, Integer> statsMap = Statistic.statsGet(List.of("/events/" + event.getId()),
                statsClient);

        if (!statsMap.isEmpty()) {
            dto.setViews(statsMap.get(event.getId()));
        }

        if (participationCounts != 0) {
            dto.setConfirmedRequests(participationCounts);
        }

        Map<Emoji, Long> rates = ratingRepository
                .findMapByEventId(event.getId());
        dto.setRates(rates);
        dto.setRating(ratingCalculation(rates, participationCounts));

        return dto;
    }

    private Integer ratingCalculation(Map<Emoji, Long> rates,
                                      Integer participationCounts) {

        if (rates.isEmpty()) {
            return 0;
        }

        return (participationCounts - rates.getOrDefault(Emoji.DISLIKE, 0L).intValue())
                * rates.getOrDefault(Emoji.LIKE, 0L).intValue();
    }
}
