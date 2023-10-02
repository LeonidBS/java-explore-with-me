package ru.practicum.ewmservice.rating.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RateDto {
    private Integer eventId;

    private Integer userId;

    private String emoji;
}
