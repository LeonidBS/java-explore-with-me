package ru.practicum.ewmservice.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    @PositiveOrZero
    private Integer id;

    @NotBlank(message = "Parameter name is empty")
    @Length(min = 1, max = 50)
    private String name;
}
