package ru.practicum.statsserver.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.statsdto.EndpointHitDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EndpointHitMapper {
    EndpointHitMapper INSTANCE = Mappers.getMapper(EndpointHitMapper.class);

    EndpointHitDto mapToDto(EndpointHit entity);

    EndpointHit mapToEntity(EndpointHitDto dto);

    List<EndpointHitDto> mapListToDto(List<EndpointHit> entityList);
}
