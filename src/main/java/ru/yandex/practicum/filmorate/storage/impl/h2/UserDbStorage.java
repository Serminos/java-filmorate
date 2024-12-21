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

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"user_id"});
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
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        int res = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(),
                Date.valueOf(user.getBirthday()), user.getId());
        return user;
    }

    @Override
    public User findById(long userId) {
        return jdbcTemplate.query("SELECT * FROM users WHERE user_id = ?",
                userRowMapper, userId).stream().findFirst().orElse(null);
    }

    @Override
    public List<User> all() {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper);
    }

    @Override
    public void deleteUser(long userId) {
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", userId);
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM users");
    }
}
