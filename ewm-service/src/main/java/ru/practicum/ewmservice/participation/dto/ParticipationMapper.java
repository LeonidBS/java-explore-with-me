package ru.practicum.ewmservice.participation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewmservice.participation.model.Participation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipationMapper {

    ParticipationMapper INSTANCE = Mappers.getMapper(ParticipationMapper.class);

    @Mapping(target = "event", source = "event.id")
    ParticipationDto mapToDto(Participation entity);

    List<ParticipationDto> mapListToDto(List<Participation> entityList);
}
