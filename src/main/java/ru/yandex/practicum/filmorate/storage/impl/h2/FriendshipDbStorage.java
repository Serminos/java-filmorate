package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.impl.h2.mappers.FriendshipRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Qualifier("friendshipDbStorage")
class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendshipRowMapper friendshipRowMapper;

    private static final String ADD = """
            INSERT INTO friendship
            (from_user_id, to_user_id, is_confirmed)
            VALUES (?, ?, ?);
            """;
    private static final String UPDATE = """
            UPDATE friendship
            SET is_confirmed = ?
            WHERE from_user_id = ? and to_user_id=?;
            """;
    private static final String DELETE = " DELETE FROM friendship WHERE from_user_id = ? AND to_user_id = ?; ";
    private static final String GET_ALL = " SELECT * FROM friendship ";
    private static final String FIND_ALL_BY_FROM_USER_ID = " SELECT * FROM friendship WHERE from_user_id = ?; ";
    private static final String USER_ID_FRIENDSHIP = """
            SELECT *
            FROM friendship
            WHERE from_user_id = ?
            """;
    private static final String OTHER_ID_FRIENDSHIP = """
            SELECT *
            FROM friendship
            WHERE from_user_id = ?
            """;
    private static final String DELETE_ALL_BY_USER_ID = """
            DELETE FROM friendship
            WHERE from_user_id = ? OR to_user_id = ?;
            """;

    @Override
    public void add(Friendship friendship) {
        jdbcTemplate.update(ADD,
                friendship.getFromUserId(), friendship.getToUserId(), friendship.isConfirmed());
    }

    @Override
    public void update(Friendship friendship) {
        jdbcTemplate.update(UPDATE, friendship.isConfirmed(), friendship.getFromUserId(), friendship.getToUserId());
    }

    @Override
    public void delete(Friendship friendship) {
        jdbcTemplate.update(DELETE, friendship.getFromUserId(), friendship.getToUserId());
    }

    @Override
    public void clear() {
        jdbcTemplate.update(" DELETE FROM friendship ");
    }

    @Override
    public List<Friendship> getAll() {
        return jdbcTemplate.query(GET_ALL, friendshipRowMapper);
    }

    @Override
    public List<Friendship> findByFromUserId(long fromUserId) {
        return jdbcTemplate.query(FIND_ALL_BY_FROM_USER_ID, friendshipRowMapper, fromUserId);
    }

    @Override
    public List<Long> findCommonFriendId(long user1Id, long user2Id) {
        return jdbcTemplate.queryForList(" SELECT DISTINCT user1.to_user_id " +
                        " FROM (" + USER_ID_FRIENDSHIP + ") user1 " +
                        " INNER JOIN (" + OTHER_ID_FRIENDSHIP + ") user2 on user1.to_user_id = user2.to_user_id ",
                Long.class, user1Id, user2Id);
    }

    @Override
    public void deleteByUserId(long userId) {
        jdbcTemplate.update(DELETE_ALL_BY_USER_ID, userId, userId);
    }
}
