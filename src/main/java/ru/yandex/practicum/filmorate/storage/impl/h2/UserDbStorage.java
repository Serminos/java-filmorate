package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.h2.mappers.UserRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Qualifier("userDbStorage")
class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    private static final String CREATE = """
            INSERT INTO users
            (email, login, name, birthday)
            VALUES (?, ?, ?, ?);
            """;
    private static final String UPDATE = """
            UPDATE users
            SET email = ?, login = ?, name = ?, birthday = ?
            WHERE user_id = ?;
            """;
    private static final String FIND_BY_ID = " SELECT * FROM users WHERE user_id = ?; ";
    private static final String GET_ALL = " SELECT * FROM users; ";
    private static final String DELETE_BY_ID_FROM_FRIENDSHIP = """
            DELETE
            FROM friendship
            WHERE from_user_id = ? OR to_user_id = ?;
            """;
    private static final String DELETE_BY_ID = " DELETE FROM users WHERE user_id = ?; ";

    @Override
    public User create(User user) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(CREATE, new String[]{"user_id"});
                    ps.setString(1, user.getEmail());
                    ps.setString(2, user.getLogin());
                    ps.setString(3, user.getName());
                    ps.setDate(4, Date.valueOf(user.getBirthday()));
                    return ps;
                }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        int res = jdbcTemplate.update(UPDATE, user.getEmail(), user.getLogin(), user.getName(),
                Date.valueOf(user.getBirthday()), user.getId());
        return user;
    }

    @Override
    public User findByUserId(long userId) {
        return jdbcTemplate.query(FIND_BY_ID, userRowMapper, userId).stream().findFirst().orElse(null);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(GET_ALL, userRowMapper);
    }

    @Override
    public void deleteByUserId(long userId) {
        jdbcTemplate.update(DELETE_BY_ID_FROM_FRIENDSHIP, userId, userId);
        jdbcTemplate.update(DELETE_BY_ID, userId);
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM users");
    }
}
