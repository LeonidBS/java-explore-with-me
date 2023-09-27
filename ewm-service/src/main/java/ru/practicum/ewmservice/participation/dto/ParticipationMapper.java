package ru.practicum.ewmservice.participation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewmservice.participation.model.Participation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipationMapper {

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationDto mapToDto(Participation entity);

    List<ParticipationDto> mapListToDto(List<Participation> entityList);
}
