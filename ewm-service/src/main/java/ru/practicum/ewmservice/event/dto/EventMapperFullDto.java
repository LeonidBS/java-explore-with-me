package ru.practicum.ewmservice.event.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewmservice.event.model.Event;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapperFullDto {

    EventMapperFullDto INSTANCE = Mappers.getMapper(EventMapperFullDto.class);

    EventFullDto mapToDto(Event entity);

    Event mapToEntity(EventFullDto dto);

    List<EventFullDto> mapListToDto(List<Event> entityList);
}


