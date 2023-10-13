package ru.practicum.ewmservice.rating.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.rating.dto.RateDto;
import ru.practicum.ewmservice.rating.model.Emoji;
import ru.practicum.ewmservice.rating.service.RatingService;

@Slf4j
@RestController
@RequestMapping(path = "/users/rate")
@RequiredArgsConstructor
@Validated
public class RatingController {
    private final RatingService ratingService;

    @PostMapping("/{raterId}/{emoji}")
    @ResponseStatus(HttpStatus.CREATED)
    public RateDto create(
            @RequestHeader("X-Explore-Event-Id") Integer eventId,
            @PathVariable Integer raterId,
            @PathVariable Emoji emoji) {

        return ratingService.create(emoji, eventId, raterId);
    }

    @PatchMapping("/{rateId}/{emoji}")
    public RateDto update(@RequestHeader("X-Explore-Event-Id") Integer eventId,
                          @PathVariable Integer rateId,
                          @PathVariable Emoji emoji) {

        return ratingService.update(emoji, eventId, rateId);
    }

}