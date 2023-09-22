package ru.practicum.ewmservice.participation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewmservice.participation.model.ParticipationRequestStatus;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationDto {

    @PositiveOrZero
    private Integer id;

    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime created;

    @PositiveOrZero
    private Integer event;

    @PositiveOrZero
    private Integer requester;

    private ParticipationRequestStatus status;
}
