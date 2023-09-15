package ru.practicum.statsserver.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.statsdto.ViewStatsDto;


import java.util.List;

@Mapper(componentModel = "spring")
public interface ViewStatsMapper {

    ViewStatsMapper INSTANCE = Mappers.getMapper(ViewStatsMapper.class);

    @Mapping(target = "app", source = "app")
    @Mapping(target = "uri", source = "uri")
    @Mapping(target = "hits", source = "hits", numberFormat = "int")
    ViewStatsDto mapToDto(ViewStats entity);

    List<ViewStatsDto> mapListToDto(List<ViewStats> entityList);
}
