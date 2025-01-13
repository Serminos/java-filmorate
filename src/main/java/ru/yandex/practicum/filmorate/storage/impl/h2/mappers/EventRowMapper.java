package ru.yandex.practicum.filmorate.storage.impl.h2.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EventRowMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(rs.getLong("eventId"))
                .userId(rs.getLong("userId"))
                .timestamp(rs.getLong("timestamp"))
                .eventType(rs.getString("eventType"))
                .operation(rs.getString("operation"))
                .entityId(rs.getLong("entityId"))
                .build();
    }

}
