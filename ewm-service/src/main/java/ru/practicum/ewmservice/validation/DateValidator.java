package ru.practicum.ewmservice.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateValidator implements ConstraintValidator<FutureInDuration, LocalDate> {
    private Duration duration;

    @Override
    public void initialize(FutureInDuration futureInDurationAnnotation) {
        duration = Duration.parse(futureInDurationAnnotation.duration());
    }

    @Override
    public boolean isValid(LocalDate eventDate,
                           ConstraintValidatorContext cxt) {
        if (eventDate != null) {
            return Duration.between(eventDate, LocalDateTime.now())
                    .compareTo(duration) > 0;
        }
        return false;
    }
}