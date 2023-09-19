package ru.practicum.ewmservice.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.event.model.Location;
import ru.practicum.ewmservice.event.model.StateAction;
import ru.practicum.ewmservice.validation.FutureInDuration;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEventUserRequest {

    String annotation;

    private Integer category;

    private String description;

    @FutureInDuration(duration = "P2H")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    private String title;
}
