package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.impl.h2.mappers.EventRowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Component("eventDbStorage")
@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;
    private final EventRowMapper eventRowMapper;
    private static final String CREATE_EVENT = """
            INSERT INTO feeds
            (user_id, timestamp, event_type, operation, entity_id)
            VALUES (?, ?, ?, ?, ?)
            """;
    private static final String FIND_USER_EVENTS_BY_ID = " SELECT * FROM feeds WHERE user_id = ?; ";

    @Override
    public void create(long userId, EventType eventType, Operation operation, long entityId) {
        long timestamp = Timestamp.from(Instant.now()).getTime();
        jdbcTemplate.update(CREATE_EVENT, userId, timestamp, eventType.name(), operation.name(), entityId);
    }

    @Override
    public List<Event> findUserEventsById(Long userId) {
        return jdbcTemplate.query(FIND_USER_EVENTS_BY_ID, eventRowMapper, userId);
    }
}