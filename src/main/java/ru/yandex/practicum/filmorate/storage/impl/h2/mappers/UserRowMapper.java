package ru.yandex.practicum.filmorate.storage.impl.h2.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("user_id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        String name = resultSet.getString("name");
        if (!name.isEmpty()) {
            user.setName(resultSet.getString("name"));
        }
        LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
        if (birthday != null) {
            user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        }
        return user;
    }
}
