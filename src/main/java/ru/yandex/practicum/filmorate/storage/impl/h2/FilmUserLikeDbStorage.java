package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmUserLike;
import ru.yandex.practicum.filmorate.storage.FilmUserLikeStorage;
import ru.yandex.practicum.filmorate.storage.impl.h2.mappers.FilmUserLikeRowMapper;

import java.util.*;


@Repository
@RequiredArgsConstructor
@Qualifier("filmUserLikeDbStorage")
class FilmUserLikeDbStorage implements FilmUserLikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmUserLikeRowMapper filmUserLikeRowMapper;

    private static final String ADD = " INSERT INTO film_user_like (film_id, user_id) VALUES (?, ?); ";
    private static final String DELETE = " DELETE FROM film_user_like WHERE film_id = ? AND user_id = ?; ";
    private static final String GET_ALL = " SELECT * FROM film_user_like; ";
    private static final String FIND_USER_LIKE = " SELECT * FROM film_user_like WHERE film_id = ?; ";
    private static final String FIND_FILM_LIKE = " SELECT * FROM film_user_like WHERE user_id = ?; ";
    private static final String FIND_USER_LIKED_FILMS_IDS = " SELECT film_id FROM film_user_like WHERE user_id = ?; ";
    private static final String DELETE_ALL_LIKES_BY_USER_ID = " DELETE FROM film_user_like WHERE user_id = ?; ";
    private static final String DELETE_ALL_LIKES_BY_FILM_ID = " DELETE FROM film_user_like WHERE film_id = ?; ";

    @Override
    public void add(long filmId, long userId) {
        jdbcTemplate.update(ADD, filmId, userId);
    }

    @Override
    public void delete(long filmId, long userId) {
        jdbcTemplate.update(DELETE, filmId, userId);
    }

    @Override
    public void clear() {
        jdbcTemplate.update(" DELETE FROM film_user_like ");
    }

    @Override
    public List<FilmUserLike> getAll() {
        return jdbcTemplate.query(GET_ALL, filmUserLikeRowMapper);
    }

    @Override
    public List<FilmUserLike> findByFilmId(long filmId) {
        return jdbcTemplate.query(FIND_USER_LIKE, filmUserLikeRowMapper, filmId);
    }

    @Override
    public List<FilmUserLike> findByUserId(long userId) {
        return jdbcTemplate.query(FIND_FILM_LIKE, filmUserLikeRowMapper, userId);
    }

    @Override
    public Set<Long> findFilmsIdByUserId(long userId) {
        return new HashSet<>(jdbcTemplate.queryForList(FIND_USER_LIKED_FILMS_IDS, Long.class, userId));
    }

    @Override
    public Set<Long> findUsersIdIntersectByFilmsLikesWithUserByUserId(long userId, Set<Long> filmsId) {
        if (filmsId.isEmpty()) {
            return new HashSet<>();
        }
        String inSql = String.join(",", Collections.nCopies(filmsId.size(), "?"));
        String sql = String.format(" SELECT user_id " +
                " FROM film_user_like WHERE user_id != %d " +
                " and film_id IN (%s) ", userId, inSql);
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), filmsId.toArray()));
    }

    @Override
    public void deleteByUserId(long userId) {
        jdbcTemplate.update(DELETE_ALL_LIKES_BY_USER_ID, userId);
    }

    @Override
    public void deleteByFilmId(long filmId) {
        jdbcTemplate.update(DELETE_ALL_LIKES_BY_FILM_ID, filmId);
    }
}
