package ru.yandex.practicum.filmorate.storage.impl.h2.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendshipRowMapper implements RowMapper<Friendship> {

    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        Friendship friendship = new Friendship();
        friendship.setFromUserId(rs.getLong("from_user_id"));
        friendship.setToUserId(rs.getLong("to_user_id"));
        friendship.setConfirmed(rs.getBoolean("is_confirmed"));
        return friendship;
    }
}
