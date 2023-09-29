package ru.practicum.ewmservice.event.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.category.dto.CategoryMapper;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventMapper;
import ru.practicum.ewmservice.event.dto.EventShortDto;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.participation.model.ParticipationRequestStatus;
import ru.practicum.ewmservice.participation.repository.ParticipationRepository;
import ru.practicum.statsclient.client.StatsClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class GetEventDto {
    private final ParticipationRepository participationRepository;
    private final StatsClient statsClient;

    public List<EventShortDto> createShortDtoList(List<Event> events) {
        List<Integer> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        List<Integer> participationCounts = participationRepository.findParticipationCountByEventIdsStatus(eventIds,
                ParticipationRequestStatus.CONFIRMED);
        List<EventShortDto> dtos = EventMapper.mapListToShortDto(events);
        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());
        Map<Integer, Integer> statsMap = Statistic.statsGet(uris, statsClient);

        for (int i = 0; i < dtos.size(); i++) {
            dtos.get(i).setCategory(CategoryMapper.INSTANCE.mapToDto(events.get(i).getCategory()));
            if (!participationCounts.isEmpty()) {
                dtos.get(i).setConfirmedRequests(participationCounts.get(i));

                if (!statsMap.isEmpty()) {
                    dtos.get(i).setViews(statsMap.get(dtos.get(i).getId()));
                }
            }
        }



        return dtos;
    }

    public List<EventFullDto> createFullDtoList(List<Event> events) {
        List<Integer> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        List<Integer> participationCounts = participationRepository.findParticipationCountByEventIdsStatus(eventIds,
                ParticipationRequestStatus.CONFIRMED);
        List<EventFullDto> dtos = EventMapper.mapListToFullDto(events);
        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());
        Map<Integer, Integer> statsMap = Statistic.statsGet(uris, statsClient);

        for (int i = 0; i < dtos.size(); i++) {
            dtos.get(i).setCategory(CategoryMapper.INSTANCE.mapToDto(events.get(i).getCategory()));
            if (!participationCounts.isEmpty()) {
                dtos.get(i).setConfirmedRequests(participationCounts.get(i));

                if (!statsMap.isEmpty()) {
                    dtos.get(i).setViews(statsMap.get(dtos.get(i).getId()));
                }
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

        return dto;
    }
}
