package ru.practicum.statsdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStatsDto {

    @NotBlank(message = "Parameter name is empty")
    private String app;

    @NotBlank(message = "Parameter name is empty")
    private String uri;

    @NotBlank(message = "Parameter name is empty")
    private Integer hits;
}
