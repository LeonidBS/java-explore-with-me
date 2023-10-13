package ru.practicum.ewmservice.rating.service;

import ru.practicum.ewmservice.rating.dto.RateDto;
import ru.practicum.ewmservice.rating.model.Emoji;

public interface RatingService {
    RateDto create(Emoji emoji, Integer eventId, Integer raterId);

    RateDto update(Emoji emoji, Integer eventId, Integer raterId);
}
