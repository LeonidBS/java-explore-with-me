package ru.practicum.ewmservice.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmservice.rating.model.Emoji;
import ru.practicum.ewmservice.rating.model.Rate;

import java.util.List;
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

    @Query("SELECT new map(r.emoji AS emoji, count(u.id) AS number)  " +
            "FROM Rate AS r " +
            "LEFT JOIN r.event e " +
            "LEFT JOIN r.rater u " +
            "WHERE e.id IN ?1 " +
            "GROUP BY r.emoji"
    )
    List<Map<Emoji, Integer>> findMapByEventIds(List<Integer> eventIds);

    @Query("SELECT new map(e.id AS eventId, count(r.emoji) AS number)  " +
            "FROM Rate AS r " +
            "LEFT JOIN r.event e " +
            "WHERE e.id IN ?1 " +
            "AND r.emoji = 'LIKE' " +
            "GROUP BY e.id"
    )
    Map<Integer, Integer> findLikesByInitiatorIdIn(List<Integer> eventIds);

    @Query("SELECT new map(e.id AS eventId, count(r.emoji) AS number)  " +
            "FROM Rate AS r " +
            "LEFT JOIN r.event e " +
            "WHERE e.id IN ?1 " +
            "AND r.emoji = 'DISLIKE' " +
            "GROUP BY e.id"
    )
    Map<Integer, Integer> findDislikesByInitiatorIdIn(List<Integer> initiatorIds);
}
