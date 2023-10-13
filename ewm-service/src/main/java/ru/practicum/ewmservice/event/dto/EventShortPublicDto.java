package ru.practicum.ewmservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.rating.model.Emoji;
import ru.practicum.ewmservice.user.dto.UserShortForPublicDto;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventShortPublicDto {

    private Integer id;

    String annotation;

    private CategoryDto category;

    private Integer confirmedRequests;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortForPublicDto initiator;

    private Boolean paid;

    private String title;

    private int views;

    private Map<Emoji, Long> rates;

    private Integer rating;
}