package ru.practicum.ewmservice.category.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewmservice.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto mapToDto(Category entity);

    Category mapToEntity(CategoryDto dto);

    List<CategoryDto> mapListToDto(List<Category> entityList);
}




