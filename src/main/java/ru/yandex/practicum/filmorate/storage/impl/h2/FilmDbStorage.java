package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.h2.mappers.FilmRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Qualifier("filmDbStorage")
class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    @Override
    public Film create(Film film) {
        String sql = " INSERT INTO film " +
                " (name, description, release_date, duration, rating_mpa_id) " +
                " VALUES (?, ?, ?, ?, ?) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"film_id"});
                    ps.setString(1, film.getName());
                    ps.setString(2, film.getDescription());
                    ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                    ps.setLong(4, film.getDuration());
                    ps.setLong(5, film.getRatingMpaId());
                    return ps;
                }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        Long filmId = film.getId();
        String sql = " UPDATE film " +
                " SET name = ?, description = ?, release_date = ?, duration = ?, rating_mpa_id = ? " +
                " WHERE film_id = ? ";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getRatingMpaId(), filmId);
        return film;
    }

    @Override
    public List<Film> all() {
        return jdbcTemplate.query("SELECT * FROM film", filmRowMapper);

    }

    @Override
    public Film findById(long filmId) {
        return jdbcTemplate.query("SELECT * FROM film WHERE film_id = ?",
                filmRowMapper, filmId).stream().findFirst().orElse(null);
    }

    @Override
    public boolean deleteLikeByUserId(long filmId, long userId) {
        return false;
    }

    @Override
    public void clear() {
        jdbcTemplate.update(" DELETE FROM film ");
    }


    @Override
    public List<Film> findByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        String sql = "SELECT * FROM film WHERE film_id IN (" + String.join(",", Collections.nCopies(ids.size(), "?")) + ")";

        return jdbcTemplate.query(sql, filmRowMapper, ids.toArray());
    }

    @Override
    public void deleteFilm(long filmId) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);

        jdbcTemplate.update("DELETE FROM film WHERE film_id = ?", filmId);
    }

}
