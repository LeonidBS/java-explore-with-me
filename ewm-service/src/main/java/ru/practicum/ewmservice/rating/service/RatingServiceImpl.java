package ru.practicum.ewmservice.rating.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.AccessDeniedException;
import ru.practicum.ewmservice.exception.IdNotFoundException;
import ru.practicum.ewmservice.rating.dto.RateDto;
import ru.practicum.ewmservice.rating.dto.RateMapper;
import ru.practicum.ewmservice.rating.model.Emoji;
import ru.practicum.ewmservice.rating.model.Rate;
import ru.practicum.ewmservice.rating.repository.RatingRepository;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RateMapper rateMapper;

    @Override
    public RateDto create(Emoji emoji, Integer eventId, Integer raterId) {

        Event event = eventRepository.findByIdFetch(eventId)
                .orElseThrow(() -> {
                    log.error("Event with ID {} has not been found", eventId);
                    return new IdNotFoundException("There is no Event with ID: " + eventId);
                });

        if (!raterId.equals(event.getInitiator().getId())) {
            User rater = userRepository.findById(raterId)
                    .orElseThrow(() -> {
                        log.error("User with ID {} has not been found", raterId);
                        return new IdNotFoundException("There is no User with ID: " + raterId);
                    });

            Rate rate = Rate.builder()
                    .event(event)
                    .rater(rater)
                    .emoji(emoji)
                    .build();

            log.debug("Rate has been created: {}", rate);
            return rateMapper.mapToDto(ratingRepository.save(rate));
        } else {
            throw new AccessDeniedException(String.format("RaterId %d is equal Event's Initiator id %d",
                    raterId, event.getInitiator().getId()));
        }
    }

    @Override
    public RateDto update(Emoji emoji, Integer eventId, Integer raterId) {

        Rate rate = ratingRepository.findByEventIdAndRaterId(eventId, raterId)
                .orElseThrow(() -> new IdNotFoundException(String.format(
                        "There is no Rate with eventId=%d and userId=%d", eventId, raterId)));

        rate.setEmoji(emoji);

            log.debug("Rate has been updated: {}", rate);
            return rateMapper.mapToDto(ratingRepository.save(rate));
    }
}
