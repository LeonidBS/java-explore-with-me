package ru.practicum.ewmservice.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.event.model.Event;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCompilationDto {

    List<Integer> events;

    private Boolean pinned;

    private String title;
}
