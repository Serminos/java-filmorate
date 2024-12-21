package ru.yandex.practicum.filmorate.storage.impl.h2.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("name"));
        String description = resultSet.getString("description");
        if (!description.isEmpty()) {
            film.setDescription(description);
        }
        LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
        if (releaseDate != null) {
            film.setReleaseDate(releaseDate);
        }
        film.setDuration(resultSet.getLong("duration"));
        film.setRatingMpaId(resultSet.getLong("rating_mpa_id"));
        return film;
    }
}
