package ru.practicum.ewmservice.compilation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewmservice.compilation.model.Compilation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    CompilationDto mapToDto(Compilation entity);

    Compilation mapToEntity(CompilationDto dto);

    List<CompilationDto> mapListToDto(List<Compilation> entityList);
}
