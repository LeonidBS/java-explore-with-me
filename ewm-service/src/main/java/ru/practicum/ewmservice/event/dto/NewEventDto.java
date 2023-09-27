package ru.practicum.ewmservice.event.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewmservice.event.model.Location;
import ru.practicum.ewmservice.validation.FutureInDuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {

    @NotBlank
    @Length(min = 20, max = 2000)
    String annotation;

    @PositiveOrZero
    private Integer category;

    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;

    @FutureInDuration(duration = "PT2H")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    @NotBlank
    @Length(min = 3, max = 120)
    private String title;
}
