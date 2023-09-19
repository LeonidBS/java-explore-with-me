package ru.practicum.ewmservice.participation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmservice.participation.model.Participation;
import ru.practicum.ewmservice.participation.model.ParticipationRequestStatus;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Integer> {
    List<Participation> findByRequesterId(Integer userId);

    List<Participation> findByRequesterIdAndEventId(Integer userId, Integer eventId);

    @Query("SELECT COUNT(*) " +
            "FROM Participation AS p " +
            "WHERE p.event.id = ?1 " +
            "GROUP BY p.event.id ")
    Integer findParticipationCountByEventId(Integer eventId);

    @Query("SELECT COUNT(*) " +
            "FROM Participation AS p " +
            "WHERE p.event.id = ?1 AND " +
            "p.status = ?2 " +
            "GROUP BY p.event.id ")
    Integer findParticipationCountByEventIdAndStatus(Integer id, ParticipationRequestStatus participationRequestStatus);

    List<Participation> findByEventId(Integer eventId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Participation p " +
            "SET p.status = ?1" +
            "WHERE p.id = ?2 ")
    void updateParticipationStatusById (String status, Integer participationId);

    List<Participation> findByIdIn(List<Integer> requestIds);
}
