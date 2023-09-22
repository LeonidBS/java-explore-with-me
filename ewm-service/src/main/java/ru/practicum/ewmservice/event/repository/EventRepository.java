package ru.practicum.ewmservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator " +
            "LEFT JOIN e.category " +
            "LEFT JOIN e.location " +
            "WHERE e.id = ?1")
    Optional<Event> findByIdFetch(Integer id);

    Page<Event> findByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetweenOrderByEventDateDescIdAsc(
            List<Integer> users, List<State> states, List<Integer> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    Page<Event> findByStateInAndEventDateBetweenOrderByEventDateDescIdAsc(
            List<State> states, LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest page);

    Page<Event> findByStateInAndCategoryIdInAndEventDateBetweenOrderByEventDateDescIdAsc(
            List<State> states, List<Integer> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest page);

    Page<Event> findByInitiatorIdInAndStateInAndEventDateBetweenOrderByEventDateDescIdAsc(
            List<Integer> users, List<State> states, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, PageRequest page);

    Page<Event> findByInitiatorIdInAndStateInAndCategoryIdInOrderByEventDateDescIdAsc(
            List<Integer> users, List<State> states, List<Integer> categories,
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

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Event e " +
            "SET e.views = e.views + 1 " +
            "WHERE e.id IN ?1 ")
    void updateViewByEventIds(List<Integer> eventIds);

    @Query("SELECT e.id " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "WHERE i.id = ?1 ")
    List<Integer> findIdByInitiatorId(Integer userId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Event e " +
            "SET e.views = e.views + 1 " +
            "WHERE e.id = ?1")
    void updateViewByEventId(Integer eventId);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator i " +
            "LEFT JOIN e.category c " +
            "WHERE e.paid IN (:paid) " +
            "AND LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "AND e.state = 'PUBLISHED' " +
            "AND c.id IN (:categories) " +
            "AND e.eventDate > :now " +
            "ORDER BY " +
            "CASE WHEN :sort = 'EVENT_DATE' THEN e.eventDate END ASC, " +
            "CASE WHEN :sort = 'VIEWS' THEN e.views END ASC"
    )
    Page<Event> findByFiltersFromNow(@Param("text") String text,
                                     @Param("categories") List<Integer> categories,
                                     @Param("paid") List<Boolean> paids,
                                     @Param("now") LocalDateTime now,
                                     @Param("sort") String sort,
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
            "ORDER BY " +
            "CASE WHEN :sort = 'EVENT_DATE' THEN e.eventDate END ASC, " +
            "CASE WHEN :sort = 'VIEWS' THEN e.views END ASC"
    )
    Page<Event> findByFiltersInDateRange(@Param("text") String text,
                                         @Param("categories") List<Integer> categories,
                                         @Param("paid") List<Boolean> paids,
                                         @Param("rangeStart") LocalDateTime rangeStart,
                                         @Param("rangeEnd") LocalDateTime rangeEnd,
                                         @Param("sort") String sort,
                                         PageRequest page);
}
