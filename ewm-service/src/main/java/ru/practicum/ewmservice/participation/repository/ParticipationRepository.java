package ru.practicum.ewmservice.participation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmservice.participation.model.Participation;
import ru.practicum.ewmservice.participation.model.ParticipationRequestStatus;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Integer> {
    List<Participation> findByRequesterId(Integer userId);

    @Query("SELECT p " +
            "FROM Participation AS p " +
            "LEFT JOIN p.event e " +
            "LEFT JOIN p.requester r " +
            "WHERE r.id = ?1 AND " +
            "e.id = ?2 ")
    List<Participation> findByRequesterIdAndEventId(Integer userId, Integer eventId);

    @Query("SELECT COUNT(*) " +
            "FROM Participation AS p " +
            "WHERE p.event.id = ?1")
    Integer findParticipationCountByEventId(Integer eventId);

    @Query("SELECT COUNT(*) " +
            "FROM Participation AS p " +
            "LEFT JOIN p.event e " +
            "WHERE e.id = ?1 AND " +
            "p.status = ?2 ")
    Integer findParticipationCountByEventIdAndStatus(Integer eventId,
                                                     ParticipationRequestStatus participationRequestStatus);

    @Query("SELECT COUNT(p.id) " +
            "FROM Participation AS p " +
            "LEFT JOIN p.event e " +
            "WHERE e.id IN ?1 " +
            "AND p.status = ?2 " +
            "GROUP BY e.id ")
    List<Integer> findParticipationCountByEventIdsStatus(List<Integer> eventId,
                                                         ParticipationRequestStatus participationRequestStatus);

    List<Participation> findByEventId(Integer eventId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Participation p " +
            "SET p.status = ?1 " +
            "WHERE p.id = ?2 ")
    void updateParticipationStatusById(ParticipationRequestStatus status, Integer participationId);

    @Query("SELECT p " +
            "FROM Participation AS p " +
            "LEFT JOIN p.event e " +
            "LEFT JOIN p.requester r " +
            "WHERE p.id IN ?1 " +
            "AND e.id = ?2 ")
    List<Participation> findByIdInAndEventId(List<Integer> requestIds, Integer eventId);
}
