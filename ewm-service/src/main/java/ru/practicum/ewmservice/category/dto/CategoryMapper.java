package ru.practicum.ewmservice.category.dto;

import org.mapstruct.Mapper;
import ru.practicum.ewmservice.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto mapToDto(Category entity);

    List<CategoryDto> mapListToDto(List<Category> entityList);
}




