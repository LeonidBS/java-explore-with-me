package ru.practicum.ewmservice.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewmservice.event.dto.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationDto {

    private Integer id;

    List<EventShortDto> events;

    private Boolean pinned;

    @Length(min = 1, max = 50)
    private String title;
}
