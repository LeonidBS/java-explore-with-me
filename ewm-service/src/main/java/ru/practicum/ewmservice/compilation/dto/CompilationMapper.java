package ru.practicum.ewmservice.compilation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewmservice.compilation.model.Compilation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    @Mapping(target = "events", source = "events")
    CompilationDto mapToDto(Compilation entity);

    List<CompilationDto> mapListToDto(List<Compilation> entityList);
}
