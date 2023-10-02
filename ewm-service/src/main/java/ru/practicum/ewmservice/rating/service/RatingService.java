package ru.practicum.ewmservice.rating.service;

import ru.practicum.ewmservice.rating.dto.RateDto;

public interface RatingService {
    RateDto create(String emoji, Integer eventId, Integer raterId);

    RateDto update(String emoji, Integer eventId, Integer raterId);
}
