package ru.practicum.ewmservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserShortDto {
    private Integer id;

    private String name;

}
