package ru.practicum.ewmservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator " +
            "LEFT JOIN e.category " +
            "LEFT JOIN e.location " +
            "WHERE e.id = ?1")
    Optional<Event> findByIdFetch(Integer id);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "LEFT JOIN e.category c " +
            "LEFT JOIN e.location l " +
            "WHERE i.id IN ?1 " +
            "AND e.state IN ?2 " +
            "AND c.id IN ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5 " +
            "ORDER BY e.eventDate DESC, e.id ASC")
    Page<Event> findByInitiatorStateCategoryEventDate(
            List<Integer> users,
            List<State> states,
            List<Integer> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable page);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "LEFT JOIN e.category c " +
            "LEFT JOIN e.location l " +
            "WHERE e.state IN ?1 " +
            "AND e.eventDate BETWEEN ?2 AND ?3 " +
            "ORDER BY e.eventDate DESC, e.id ASC")
    Page<Event> findByStateEventDate(
            List<State> states,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            PageRequest page);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "LEFT JOIN e.category c " +
            "LEFT JOIN e.location l " +
            "WHERE e.state IN ?1 " +
            "AND c.id IN ?2 " +
            "AND e.eventDate BETWEEN ?3 AND ?4 " +
            "ORDER BY e.eventDate DESC, e.id ASC")
    Page<Event> findByStateCategoryEventDate(
            List<State> states,
            List<Integer> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            PageRequest page);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "LEFT JOIN e.category c " +
            "LEFT JOIN e.location l " +
            "WHERE i.id IN ?1 " +
            "AND e.state IN ?2 " +
            "AND e.eventDate BETWEEN ?3 AND ?4 " +
            "ORDER BY e.eventDate DESC, e.id ASC")
    Page<Event> findByInitiatorStateEventDate(
            List<Integer> users,
            List<State> states,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            PageRequest page);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "LEFT JOIN e.category c " +
            "LEFT JOIN e.location l " +
            "WHERE i.id IN ?1 " +
            "AND e.state IN ?2 " +
            "AND c.id IN ?3 " +
            "ORDER BY e.eventDate DESC, e.id ASC")
    Page<Event> findByInitiatorStateCategory(
            List<Integer> users,
            List<State> states,
            List<Integer> categories,
            PageRequest page);

    Page<Event> findByInitiatorIdInAndStateInOrderByEventDateDescIdAsc(
            List<Integer> users, List<State> states, PageRequest page);

    Page<Event> findByStateInAndCategoryIdInOrderByEventDateDescIdAsc(
            List<State> states, List<Integer> categories, PageRequest page);

    Page<Event> findByStateInOrderByEventDateDescIdAsc(List<State> states, PageRequest page);

    Page<Event> findByInitiatorIdOrderByEventDateDescIdAsc(Integer initiatorId, Pageable page);

    List<Event> findByInitiatorIdOrderByEventDateDescIdAsc(Integer initiatorId);

    List<Event> findByCategoryId(Integer catId);

    List<Event> findByIdIn(List<Integer> eventIds);

    @Query
            ("SELECT e.id " +
                    "FROM Event AS e " +
                    "WHERE e.id = ?1 ")
    Integer findInitiatorIdById(Integer eventId);

    @Query
            ("SELECT e.state " +
                    "FROM Event AS e " +
                    "WHERE e.id = ?1 ")
    State findStateById(Integer eventId);

    @Query
            ("SELECT e.participantLimit " +
                    "FROM Event AS e " +
                    "WHERE e.id = ?1 ")
    Integer findParticipantLimitById(Integer eventId);

    @Query
            ("SELECT e.requestModeration " +
                    "FROM Event AS e " +
                    "WHERE e.id = ?1 ")
    Boolean findRequestModerationById(Integer eventId);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "LEFT JOIN e.category c " +
            "WHERE e.paid IN (:paid) " +
            "AND LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "AND e.state = 'PUBLISHED' " +
            "AND c.id IN (:categories) " +
            "AND e.eventDate > :now " +
            "ORDER BY e.eventDate ASC "
    )
    Page<Event> findByFiltersFromNow(@Param("text") String text,
                                     @Param("categories") List<Integer> categories,
                                     @Param("paid") List<Boolean> paids,
                                     @Param("now") LocalDateTime now,
                                     PageRequest page);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "LEFT JOIN e.category c " +
            "WHERE e.paid IN (:paid) " +
            "AND LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "AND e.state = 'PUBLISHED' " +
            "AND c.id IN (:categories) " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "ORDER BY e.eventDate ASC "
    )
    Page<Event> findByFiltersInDateRange(@Param("text") String text,
                                         @Param("categories") List<Integer> categories,
                                         @Param("paid") List<Boolean> paids,
                                         @Param("rangeStart") LocalDateTime rangeStart,
                                         @Param("rangeEnd") LocalDateTime rangeEnd,
                                         PageRequest page);

    @Query("SELECT e.id " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "WHERE i.id = ?1 " +
            "AND e.state = 'PUBLISHED' ")
    List<Integer> findIdsByInitiatorStateConfirmedInPast(
            Integer initiatorId);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "WHERE i.id = ?1 " +
            "AND e.state = 'PUBLISHED' ")
    List<Event> findByInitiatorStateConfirmedInPast(
            Integer initiatorId);

    @Query("SELECT i.id " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "WHERE e.id IN ?1 ")
    List<Integer> findDistinctInitiatorIdByIdIn(List<Integer> dtoIds);

    @Query("SELECT e.id, i.id " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "WHERE i.id IN ?1 " +
            "AND e.state = 'PUBLISHED' ")
    List<Object[]> findIdsByInitiatorInStateConfirmedInPast0(
            List<Integer> initiatorIds);

    default Map<Integer, Integer> findIdsByInitiatorInStateConfirmedInPast(List<Integer> initiatorIds) {
        return findIdsByInitiatorInStateConfirmedInPast0(initiatorIds).stream()
                .collect(
                        Collectors.toMap(
                                o -> (Integer) o[0],
                                o -> (Integer) o[1]
                        )
                );
    }

    @Query("SELECT e.id " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "WHERE i.id IN ?1 " +
            "AND e.state = 'PUBLISHED' ")
    List<Integer> findIdsByInitiatorInStateConfirmedInPastList(
            List<Integer> initiatorIds);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "WHERE i.id IN ?1 " +
            "AND e.state = 'PUBLISHED' " +
            "AND e.eventDate < ?2 ")
    List<Event> findByInitiatorInStateConfirmedInPastList(
            List<Integer> initiatorIds,
            LocalDateTime rangeStart);

    @Query("SELECT e.id AS id, i.id AS initiatorId " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "WHERE i.id IN ?1 " +
            "AND e.state = 'PUBLISHED' ")
    List<Object[]> findByInitiatorInStateConfirmedInPast0(
            List<Integer> initiatorIds);

    default Map<Integer, Integer> findByInitiatorInStateConfirmedInPast(List<Integer> initiatorIds) {
        return findByInitiatorInStateConfirmedInPast0(initiatorIds).stream()
                .collect(
                        Collectors.toMap(
                                o -> (Integer) o[0],
                                o -> (Integer) o[1]
                        )
                );
    }

    @Query("SELECT e.id, e.participantLimit  " +
            "FROM Event AS e " +
            "WHERE e.id IN ?1 "
    )
    List<Object[]> findParticipantLimitsByIdIn0(List<Integer> eventIds);

    default Map<Integer, Integer> findParticipantLimitsByIdIn(List<Integer> eventIds) {
        return findParticipantLimitsByIdIn0(eventIds).stream()
                .collect(
                        Collectors.toMap(
                                o -> (Integer) o[0],
                                o -> (Integer) o[1]
                        )
                );
    }
}
