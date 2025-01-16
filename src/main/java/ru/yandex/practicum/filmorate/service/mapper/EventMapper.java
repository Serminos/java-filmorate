package ru.yandex.practicum.filmorate.service.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.model.Event;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static EventDto mapToEventDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setEventId(event.getEventId());
        eventDto.setUserId(event.getUserId());
        eventDto.setTimestamp(event.getTimestamp());
        eventDto.setEventType(event.getEventType());
        eventDto.setOperation(event.getOperation());
        eventDto.setEntityId(event.getEntityId());
        return eventDto;
    }

    public static Event mapToEvent(EventDto eventDto) {
        Event event = new Event();
        event.setEventId(eventDto.getEventId());
        event.setUserId(eventDto.getUserId());
        event.setTimestamp(eventDto.getTimestamp());
        event.setEventType(eventDto.getEventType());
        event.setOperation(eventDto.getOperation());
        event.setEntityId(eventDto.getEntityId());
        return event;
    }
}
