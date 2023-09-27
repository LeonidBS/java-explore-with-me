package ru.practicum.ewmservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewmservice.event.model.Location;
import ru.practicum.ewmservice.event.model.StateAdminAction;
import ru.practicum.ewmservice.validation.FutureInDuration;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEventAdminRequest {

    @Length(min = 20, max = 2000)
    String annotation;

    private Integer category;

    @Length(min = 20, max = 7000)
    private String description;

    @FutureInDuration(duration = "PT2H")
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private StateAdminAction stateAction;

    @Length(min = 3, max = 120)
    private String title;
}
