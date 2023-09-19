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

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetweenOrderByEventDate(
            List<Integer> users, List<State> states, List<Integer> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    Page<Event> findByInitiatorId(Integer initiatorId, Pageable page);

    List<Event> findByCategoryId(Integer catId);

    List<Event> findByIdIn(List<Integer> eventIds);

    Integer findInitiatorIdById(Integer eventId);

    State findStateById(Integer eventId);

    Integer findParticipantLimitById(Integer eventId);

    Boolean findRequestModerationById(Integer eventId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Event e " +
            "SET e.views = e.views + 1" +
            "WHERE e.id IN ?2 ")
    void updateViewByEventIds(List<Integer> eventIds);

    List<Integer> findIdByInitiatorId(Integer userId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Event e " +
            "SET e.views = e.views + 1" +
            "WHERE e.id = ?2 ")
    void updateViewByEventId(Integer eventId);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator " +
            "LEFT JOIN e.category " +
            "WHERE LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "AND e.state = 'PULISHED' " +
            "AND e.category IN (:categories) " +
            "AND e.paid = :paid " +
            "AND e.eventDate > :now " +
            "ORDER BY " +
            "CASE WHEN :sort = 'EVENT_DATE' THEN e.eventDate END ASC, " +
            "CASE WHEN :sort = 'VIEWS' THEN e.views END ASC"
    )
    List<Event> findByFiltersFromNow(@Param("text") String text,
                                     @Param("categories") List<Integer> categories,
                                     @Param("paid") Boolean paid,
                                     @Param("now") LocalDateTime now,
                                     @Param("sort") String sort,
                                     PageRequest page);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN e.initiator " +
            "LEFT JOIN e.category " +
            "WHERE LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "AND e.state = 'PULISHED' " +
            "AND e.category IN (:categories) " +
            "AND e.paid = :paid " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "ORDER BY " +
            "CASE WHEN :sort = 'EVENT_DATE' THEN e.eventDate END ASC, " +
            "CASE WHEN :sort = 'VIEWS' THEN e.views END ASC"
    )
    List<Event> findByFiltersInDateRange(@Param("text") String text,
                                         @Param("categories") List<Integer> categories,
                                         @Param("paid") Boolean paid,
                                         @Param("rangeStart") LocalDateTime rangeStart,
                                         @Param("rangeEnd") LocalDateTime rangeEnd,
                                         @Param("sort") String sort, PageRequest page);

}
