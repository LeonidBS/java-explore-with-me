package ru.practicum.statsserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statsserver.model.EndpointHit;
import ru.practicum.statsserver.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {

    @Query("SELECT new ru.practicum.statsserver.model.ViewStats(eh.app, eh.uri," +
            " COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "AND eh.uri IN ?3 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC ")
    Page<ViewStats> findViewByListStatsWithUniqueIp(LocalDateTime start, LocalDateTime end,
                                                    List<String> uris, PageRequest page);

    @Query("SELECT new ru.practicum.statsserver.model.ViewStats(eh.app, eh.uri," +
            " COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC ")
    Page<ViewStats> findAllViewStatsWithUniqueIp(LocalDateTime start, LocalDateTime end, PageRequest page);

    @Query("SELECT new ru.practicum.statsserver.model.ViewStats(eh.app, eh.uri," +
            " COUNT(eh.ip)) " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "AND eh.uri IN ?3 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC ")
    Page<ViewStats> findViewStatsByList(LocalDateTime start, LocalDateTime end,
                                        List<String> uris, PageRequest page);

    @Query("SELECT new ru.practicum.statsserver.model.ViewStats(eh.app, eh.uri," +
            " COUNT(eh.ip)) " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "AND LOWER(eh.uri) LIKE LOWER(CONCAT('%', ?3, '%')) " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC ")
    Page<ViewStats> findViewStatsByListLike(LocalDateTime start, LocalDateTime end,
                                            String uri, PageRequest page);

    @Query("SELECT new ru.practicum.statsserver.model.ViewStats(eh.app, eh.uri," +
            " COUNT(eh.ip)) " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC ")
    Page<ViewStats> findAllViewStats(LocalDateTime start, LocalDateTime end, PageRequest page);

    @Query("SELECT new ru.practicum.statsserver.model.ViewStats(eh.app, eh.uri," +
            " COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.uri IN ?3 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC ")
    Page<ViewStats> findViewByListStatsWithUniqueIpWithoutDates(List<String> uris, PageRequest page);

    @Query("SELECT new ru.practicum.statsserver.model.ViewStats(eh.app, eh.uri," +
            " COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit AS eh " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC ")
    Page<ViewStats> findAllViewStatsWithUniqueIpWithoutDates(PageRequest page);

    @Query("SELECT new ru.practicum.statsserver.model.ViewStats(eh.app, eh.uri," +
            " COUNT(eh.ip)) " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.uri IN ?3 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC ")
    Page<ViewStats> findViewStatsByListWithoutDates(List<String> uris, PageRequest page);

    @Query("SELECT new ru.practicum.statsserver.model.ViewStats(eh.app, eh.uri," +
            " COUNT(eh.ip)) " +
            "FROM EndpointHit AS eh " +
            "WHERE LOWER(eh.uri) LIKE LOWER(CONCAT('%', ?1, '%')) " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC ")
    Page<ViewStats> findViewStatsByListWithoutDatesLike(String uri, PageRequest page);

    @Query("SELECT new ru.practicum.statsserver.model.ViewStats(eh.app, eh.uri," +
            " COUNT(eh.ip)) " +
            "FROM EndpointHit AS eh " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC ")
    Page<ViewStats> findAllViewStatsWithoutDates(PageRequest page);

    @Query("SELECT count(*) " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.ip LIKE ?1 " +
            "AND eh.uri LIKE ?2 ")
    Integer findHitByIpAndUri(String ip, String uri);
}
