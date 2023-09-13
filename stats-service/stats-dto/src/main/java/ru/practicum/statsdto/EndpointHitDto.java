package ru.practicum.statsdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndpointHitDto {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @PositiveOrZero
    private Integer id;

    @NotBlank(message = "Parameter name is empty")
    private String app;

    @NotBlank(message = "Parameter name is empty")
    private String uri;

    @NotBlank(message = "Parameter name is empty")
    private String ip;

    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime timestamp;
}
