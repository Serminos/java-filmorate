package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventStorage {

    void create(long userId, EventType eventType, Operation operation, long entityId);

    List<Event> getUserEvents(Long id);
}
