package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
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
    public List<Film> findByNameContainingIgnoreCase(String query) {
        return jdbcTemplate.query("SELECT * FROM film WHERE lower(name) like '%'||lower(?)||'%'",
                filmRowMapper, query);
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
    public List<Film> findFilmsByDirector(int directorId, String sortBy) {

        final String GET_FILMS_BY_DIRECTOR_SORT_BY_YEAR = """
                SELECT
                        f.film_id,
                        f.name,
                        f.description,
                        f.release_date,
                        f.duration,
                        f.rating_mpa_id
                    FROM film f
                    JOIN film_director fd ON f.film_id = fd.film_id
                    WHERE fd.director_id = ?
                    ORDER BY YEAR(f.release_date) ASC;
                """;

        final String GET_FILMS_BY_DIRECTOR_SORT_BY_LIKES = """
                SELECT
                        f.film_id,
                        f.name,
                        f.description,
                        f.release_date,
                        f.duration,
                        f.rating_mpa_id
                FROM film f
                JOIN film_director fd ON f.film_id = fd.film_id
                LEFT JOIN film_user_like fl ON f.film_id = fl.film_id
                WHERE fd.director_id = ?
                GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration, f.rating_mpa_id
                ORDER BY COUNT(fl.user_id) DESC;
                """;

        String query;
        if ("year".equals(sortBy)) {
            query = GET_FILMS_BY_DIRECTOR_SORT_BY_YEAR;
        } else if ("likes".equals(sortBy)) {
            query = GET_FILMS_BY_DIRECTOR_SORT_BY_LIKES;
        } else {
            throw new BadRequestException("Сортировка может быть только по двум параметрам: year или likes");
        }

        return jdbcTemplate.query(query, new Object[]{directorId}, filmRowMapper);
    }  
        
    @Override
    public List<Film> findPopularByFilmIdIn(List<Long> filmIds) {
        if (filmIds.isEmpty()) {
            return List.of();
        }
        String inSql = String.join(",", Collections.nCopies(filmIds.size(), "?"));

        String sql = " SELECT f.* " +
                " FROM FILM f " +
                " LEFT JOIN FILM_USER_LIKE AS ful ON f.film_id = ful.film_id " +
                " WHERE f.film_id IN (" + inSql + ") " +
                " GROUP BY f.film_id " +
                " ORDER BY COUNT(ful.film_id) desc ";

        return jdbcTemplate.query(sql, filmRowMapper, filmIds.toArray());
    }

    @Override
    public void deleteFilm(long filmId) {
        jdbcTemplate.update("DELETE FROM film_user_like WHERE film_id = ?", filmId);

        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);

        jdbcTemplate.update("DELETE FROM film WHERE film_id = ?", filmId);
    }

    @Override
    public List<Film> findPopular(long limit) {
        String sql = " SELECT f.* " +
                " FROM FILM f " +
                " LEFT JOIN FILM_USER_LIKE AS ful ON f.film_id = ful.film_id " +
                " GROUP BY f.film_id " +
                " ORDER BY COUNT(ful.film_id) desc " +
                " LIMIT ? ";
        return jdbcTemplate.query(sql, filmRowMapper, limit);
    }
}
