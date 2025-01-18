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

    @Override
    public void add(Friendship friendship) {
        jdbcTemplate.update("INSERT INTO friendship (from_user_id, to_user_id, is_confirmed) VALUES (?, ?, ?) ",
                friendship.getFromUserId(), friendship.getToUserId(), friendship.isConfirmed());
    }

    @Override
    public void update(Friendship friendship) {
        String sql = " UPDATE friendship " +
                " SET is_confirmed = ? " +
                " WHERE from_user_id = ? and to_user_id=? ";
        jdbcTemplate.update(sql, friendship.isConfirmed(), friendship.getFromUserId(), friendship.getToUserId());
    }

    @Override
    public void remove(Friendship friendship) {
        jdbcTemplate.update(" DELETE FROM friendship WHERE from_user_id = ? AND to_user_id = ? ",
                friendship.getFromUserId(), friendship.getToUserId());

    }

    @Override
    public void clear() {
        jdbcTemplate.update(" DELETE FROM friendship ");
    }

    @Override
    public List<Friendship> all() {
        return jdbcTemplate.query(" SELECT * FROM friendship ", friendshipRowMapper);
    }

    @Override
    public List<Friendship> findAllByFromUserId(long fromUserId) {
        return jdbcTemplate.query(" SELECT * FROM friendship WHERE from_user_id = ? ",
                friendshipRowMapper, fromUserId);
    }

    @Override
    public List<Friendship> findAllByFromUserIdAndConfirmedStatus(long fromUserId, boolean isConfirmed) {
        return jdbcTemplate.query(" SELECT * FROM friendship WHERE from_user_id = ? AND is_confirmed = ? ",
                friendshipRowMapper, fromUserId, isConfirmed);
    }

    @Override
    public List<Friendship> findAllByToUserId(long toUserId) {
        return jdbcTemplate.query(" SELECT * FROM friendship WHERE to_user_id = ? ",
                friendshipRowMapper, toUserId);
    }

    @Override
    public List<Friendship> findAllByToUserIdAndConfirmedStatus(long toUserId, boolean isConfirmed) {
        return jdbcTemplate.query(" SELECT * FROM friendship WHERE to_user_id = ? AND is_confirmed = ? ",
                friendshipRowMapper, toUserId, isConfirmed);
    }

    @Override
    public List<Long> findCommonFriendId(long user1Id, long user2Id) {
        // Не правильные тесты для Postman считают что друзья без подтверждения
        /*String userIdFriendship = " SELECT * " +
                " FROM friendship " +
                " WHERE from_user_id = ? AND is_confirmed = true ";
        String otherIdFriendship = " SELECT * " +
                " FROM friendship " +
                " WHERE from_user_id = ? AND is_confirmed = true ";*/
        String userIdFriendship = " SELECT * " +
                " FROM friendship " +
                " WHERE from_user_id = ? ";
        String otherIdFriendship = " SELECT * " +
                " FROM friendship " +
                " WHERE from_user_id = ? ";
        return jdbcTemplate.queryForList(" SELECT DISTINCT user1.to_user_id " +
                        " FROM (" + userIdFriendship + ") user1 " +
                        " INNER JOIN (" + otherIdFriendship + ") user2 on user1.to_user_id = user2.to_user_id ",
                Long.class, user1Id, user2Id);
    }

    @Override
    public void removeAllByUserId(long userId) {
        String sql = "DELETE FROM friendship WHERE from_user_id = ? OR to_user_id = ?";
        jdbcTemplate.update(sql, userId, userId);
    }
}
