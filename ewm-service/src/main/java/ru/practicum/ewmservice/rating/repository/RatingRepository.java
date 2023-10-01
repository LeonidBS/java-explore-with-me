package ru.practicum.ewmservice.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmservice.rating.model.Emoji;
import ru.practicum.ewmservice.rating.model.Rate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface RatingRepository extends JpaRepository<Rate, Long> {

    Optional<Rate> findByEventIdAndRaterId(Integer eventId, Integer raterId);

    @Query(value = "SELECT r.emoji, count(r.emoji) " +
            "FROM Rate AS r " +
            "LEFT JOIN r.event e " +
            "LEFT JOIN r.rater u " +
            "WHERE e.id = ?1 " +
            "GROUP BY r.emoji"
    )
    List<Object[]> findMapByEventId0(Integer eventId);

    default Map<Emoji, Long> findMapByEventId(Integer eventId) {
        return findMapByEventId0(eventId).stream()
                .collect(
                        Collectors.toMap(
                                o -> Emoji.valueOf((String) o[0]),
                                o -> (Long) o[1]
                        )
                );
    }


    @Query("SELECT e.id, r.emoji, count(r.emoji)  " +
            "FROM Rate AS r " +
            "LEFT JOIN r.event e " +
            "LEFT JOIN r.rater u " +
            "WHERE e.id IN ?1 " +
            "GROUP BY e.id, r.emoji "
    )
    List<Object[]> findMapByEventIds0(List<Integer> eventIds);

    default Map<Integer, Map<Emoji, Long>> findMapByEventIds(List<Integer> eventIds) {
        return findMapByEventIds0(eventIds).stream()
                .collect(
                        Collectors.groupingBy(
                                o -> (Integer) o[0],
                                Collectors.toMap(
                                        o -> Emoji.valueOf((String) o[1]),
                                        o -> (Long) o[2]
                                )
                        )
                );
    }

    @Query("SELECT e.id, count(r.emoji) " +
            "FROM Rate AS r " +
            "LEFT JOIN r.event e " +
            "LEFT JOIN r.rater u " +
            "WHERE e.id IN ?1 " +
            "AND r.emoji = 'LIKE' " +
            "GROUP BY e.id, r.emoji "
    )
    List<Object[]> findLikesByInitiatorIdIn0(List<Integer> eventIds);

    default Map<Integer, Long> findLikesByInitiatorIdIn(List<Integer> eventIds) {
        return findLikesByInitiatorIdIn0(eventIds).stream()
                .collect(
                        Collectors.toMap(
                                o -> (Integer) o[0],
                                o -> (Long) o[1]
                        )
                );
    }

    @Query("SELECT e.id, count(r.emoji) " +
            "FROM Rate AS r " +
            "LEFT JOIN r.event e " +
            "LEFT JOIN r.rater u " +
            "WHERE e.id IN ?1 " +
            "AND r.emoji = 'DISLIKE' " +
            "GROUP BY e.id, r.emoji "
    )
    List<Object[]> findDislikesByInitiatorIdIn0(List<Integer> eventIds);

    default Map<Integer, Long> findDislikesByInitiatorIdIn(List<Integer> eventIds) {
        return findDislikesByInitiatorIdIn0(eventIds).stream()
                .collect(
                        Collectors.toMap(
                                o -> (Integer) o[0],
                                o -> (Long) o[1]
                        )
                );
    }
}
