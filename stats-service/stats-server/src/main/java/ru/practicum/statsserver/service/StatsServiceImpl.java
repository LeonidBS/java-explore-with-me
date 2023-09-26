package ru.practicum.statsserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statsdto.EndpointHitDto;
import ru.practicum.statsdto.ViewStatsDto;
import ru.practicum.statsserver.exception.MyValidationException;
import ru.practicum.statsserver.model.EndpointHitMapper;
import ru.practicum.statsserver.model.ViewStatsMapper;
import ru.practicum.statsserver.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final EndpointHitMapper eMapper;
    private final ViewStatsMapper vMapper;

    @Override
    public List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris,
                                        boolean unique, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        if (start != null && end != null) {
            if (!end.isAfter(start)) {
                log.error("Start must be before End");
                throw new MyValidationException("Start must be before End");
            }
        }

        if (start == null && end != null) {
            log.error("Start is NULL, while END is {}", end);
            throw new MyValidationException("Start is NULL, while End is " + end);
        }

        if (end == null && start != null) {
            log.error("End is NULL, while Start is {}", end);
            throw new MyValidationException("End is NULL, while Start is " + end);
        }


        if (unique) {
            if (uris != null && !uris.isEmpty()) {

                if (start != null && end != null) {
                    return vMapper.mapListToDto(statsRepository
                            .findViewByListStatsWithUniqueIp(start, end, uris, page).toList());
                } else {
                    return vMapper.mapListToDto(statsRepository
                            .findViewByListStatsWithUniqueIpWithoutDates(uris, page).toList());
                }

            } else {

                if (start != null && end != null) {
                    return vMapper.mapListToDto(statsRepository
                            .findAllViewStatsWithUniqueIp(start, end, page).toList());
                } else {
                    return vMapper.mapListToDto(statsRepository
                            .findAllViewStatsWithUniqueIpWithoutDates(page).toList());
                }
            }
        } else {
            if (uris != null && !uris.isEmpty()) {

                if (start != null && end != null) {
                    if (uris.size() > 1) {
                        return vMapper.mapListToDto(statsRepository
                                .findViewStatsByList(start, end, uris, page).toList());
                    } else {
                        return vMapper.mapListToDto(statsRepository
                                .findViewStatsByListLike(start, end, uris.get(0), page).toList());
                    }
                } else {
                    if (uris.size() > 1) {
                        return vMapper.mapListToDto(statsRepository
                                .findViewStatsByListWithoutDates(uris, page).toList());
                    } else {
                        return vMapper.mapListToDto(statsRepository
                                .findViewStatsByListWithoutDatesLike(uris.get(0), page).toList());
                    }
                }

            } else {

                if (start != null && end != null) {
                        return vMapper.mapListToDto(statsRepository
                                .findAllViewStats(start, end, page).toList());
                } else {
                    return vMapper.mapListToDto(statsRepository
                            .findAllViewStatsWithoutDates(page).toList());
                }

            }
        }
    }

    @Override
    @Transactional
    public EndpointHitDto addStats(EndpointHitDto endpointHitDto) {
        return eMapper.mapToDto(statsRepository.save(eMapper.mapToEntity(endpointHitDto)));
    }

    @Override
    public Integer findHitByIpAndUri(String ip, String uri) {
        return statsRepository.findHitByIpAndUri(ip, uri);
    }


}