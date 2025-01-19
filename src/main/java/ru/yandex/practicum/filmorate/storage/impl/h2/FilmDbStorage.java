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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Qualifier("filmDbStorage")
class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    private static final String CREATE = """
            INSERT INTO film
            (name, description, release_date, duration, rating_mpa_id)
            VALUES (?, ?, ?, ?, ?);
            """;
    private static final String UPDATE = """
            UPDATE film
            SET name = ?, description = ?, release_date = ?, duration = ?, rating_mpa_id = ?
            WHERE film_id = ?;
            """;
    private static final String GET_ALL = " SELECT * FROM film; ";
    private static final String FIND_BY_FILM_ID = " SELECT * FROM film WHERE film_id = ?; ";
    private static final String FIND_BY_NAME = " SELECT * FROM film WHERE lower(name) like '%'||lower(?)||'%'; ";
    private static final String GET_FILMS_BY_DIRECTOR_SORT_BY_YEAR = """
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

    private static final String GET_FILMS_BY_DIRECTOR_SORT_BY_LIKES = """
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
    private static final String GET_FILMS_IDS_BY_YEAR = """
            SELECT film_id
            FROM film
            WHERE release_date >= ? AND release_date < ?;
            """;
    private static final String DELETE_BY_ID = """
                DELETE FROM film_user_like WHERE film_id = ?;
                DELETE FROM film_genre WHERE film_id = ?;
                DELETE FROM film WHERE film_id = ?;
            """;
    private static final String FIND_POPULAR = """
            SELECT f.film_id
            FROM FILM f
            LEFT JOIN FILM_USER_LIKE AS ful ON f.film_id = ful.film_id
            GROUP BY f.film_id
            ORDER BY COUNT(ful.film_id) DESC
            LIMIT ?;
            """;

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(CREATE, new String[]{"film_id"});
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
        jdbcTemplate.update(UPDATE, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getRatingMpaId(), filmId);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(GET_ALL, filmRowMapper);

    }

    @Override
    public Film findByFilmId(long filmId) {
        return jdbcTemplate.query(FIND_BY_FILM_ID, filmRowMapper, filmId).stream().findFirst().orElse(null);
    }

    @Override
    public List<Film> findByNameContainingIgnoreCase(String query) {
        return jdbcTemplate.query(FIND_BY_NAME, filmRowMapper, query);
    }

    @Override
    public void clear() {
        jdbcTemplate.update(" DELETE FROM film ");
    }

    @Override
    public List<Film> findByFilmIdIn(List<Long> filmsId) {
        if (filmsId.isEmpty()) {
            return List.of();
        }

        String sql = "SELECT * FROM film WHERE film_id IN (" + String.join(",", Collections.nCopies(filmsId.size(), "?")) + ")";

        return jdbcTemplate.query(sql, filmRowMapper, filmsId.toArray());
    }

    @Override
    public List<Film> findByDirectorIdWithSort(long directorId, String sortBy) {

        String query;
        if ("year".equals(sortBy)) {
            query = GET_FILMS_BY_DIRECTOR_SORT_BY_YEAR;
        } else if ("likes".equals(sortBy)) {
            query = GET_FILMS_BY_DIRECTOR_SORT_BY_LIKES;
        } else {
            throw new BadRequestException("Сортировка может быть только по двум параметрам: year или likes");
        }
        return jdbcTemplate.query(query, filmRowMapper, directorId);
    }

    @Override
    public List<Long> findFilmsIdByYear(int year) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate startOfNextYear = LocalDate.of(year + 1, 1, 1);
        return jdbcTemplate.query(GET_FILMS_IDS_BY_YEAR, (rs, rowNum) -> rs.getLong("film_id"),
                startOfYear, startOfNextYear);
    }

    @Override
    public List<Film> findPopularByFilmIdIn(List<Long> filmIds, long limit) {
        if (filmIds.isEmpty()) {
            return List.of();
        }
        String inSql = String.join(",", Collections.nCopies(filmIds.size(), "?"));

        String sql = String.format(" SELECT f.* " +
                " FROM FILM f " +
                " LEFT JOIN FILM_USER_LIKE AS ful ON f.film_id = ful.film_id " +
                " WHERE f.film_id IN (%s) " +
                " GROUP BY f.film_id " +
                " ORDER BY COUNT(ful.film_id) desc ", inSql);
        if (limit > 0) {
            sql += String.format(" LIMIT %d ", limit);
        }

        return jdbcTemplate.query(sql, filmRowMapper, filmIds.toArray());
    }

    @Override
    public void deleteByFilmId(long filmId) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(DELETE_BY_ID);
            ps.setLong(1, filmId);
            ps.setLong(2, filmId);
            ps.setLong(3, filmId);
            return ps;
        });
    }

    @Override
    public List<Long> findPopularFilmsIdWithLimit(long limit) {
        return jdbcTemplate.query(FIND_POPULAR, (rs, rowNum) -> rs.getLong("film_id"), limit);
    }
}
