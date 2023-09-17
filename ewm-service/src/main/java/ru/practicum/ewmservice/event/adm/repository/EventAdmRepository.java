package ru.practicum.ewmservice.event.adm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventAdmRepository extends JpaRepository<Event, Integer> {

    List<Event> findByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetweenOrderByEventDate(
            List<Integer> users, List<State> states, List<Integer> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    List<Event> findByInitiatorId(Integer initiatorId);

    List<Event> findByCategoryId(Integer catId);

    List<Event> findByIdIn(List<Integer> eventIds);
}
