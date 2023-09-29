package ru.practicum.ewmservice.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmservice.rating.model.Emoji;
import ru.practicum.ewmservice.rating.model.Rate;

import java.util.Map;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rate, Long> {

    Optional<Rate> findByEventIdAndRaterId(Integer eventId, Integer raterId);

    @Query("SELECT new map(r.emoji AS emoji, count(u.id) AS number)  " +
            "FROM Rate AS r " +
            "LEFT JOIN r.event e " +
            "LEFT JOIN r.rater u " +
            "WHERE e.id = ?1 " +
            "GROUP BY r.emoji"
    )
    Map<Emoji, Integer> findMapByEventId(Integer eventId);
}
