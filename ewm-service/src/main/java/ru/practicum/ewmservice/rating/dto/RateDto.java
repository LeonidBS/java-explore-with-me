package ru.practicum.ewmservice.rating.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.rating.model.Emoji;
import ru.practicum.ewmservice.user.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RateDto {
    private Integer eventId;

    private Integer userId;

    private String emoji;
}
