package ru.practicum.ewmservice.rating.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewmservice.rating.model.Rate;

@Mapper(componentModel = "spring")
public interface RateMapper {

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "userId", source = "rater.id")
    RateDto mapToDto(Rate entity);

}

