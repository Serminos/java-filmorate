package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private Long eventId;
    private Long userId;
    private Long timestamp;
    private EventType eventType;
    private Operation operation;
    private Long entityId;
}