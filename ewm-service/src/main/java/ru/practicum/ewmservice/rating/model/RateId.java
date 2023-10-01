package ru.practicum.ewmservice.rating.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.user.model.User;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class RateId implements Serializable {

    private Event event;
    private User rater;
}
