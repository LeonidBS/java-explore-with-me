package ru.practicum.ewmservice.event.adm.service;

import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.event.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdmService {

    List<EventFullDto> findEventsByAdmin(List<Integer> users, List<State> states, List<Integer> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto update(UpdateEventAdminRequest updateEventAdminRequest, Integer eventId);

    EventFullDto timepatch(Integer eventId, Integer hours);
}
