package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmUserLike;
import ru.yandex.practicum.filmorate.storage.FilmUserLikeStorage;
import ru.yandex.practicum.filmorate.storage.impl.h2.mappers.FilmUserLikeRowMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Qualifier("filmUserLikeDbStorage")
class FilmUserLikeDbStorage implements FilmUserLikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmUserLikeRowMapper filmUserLikeRowMapper;

    @Override
    public void add(long filmId, long userId) {
        jdbcTemplate.update(" INSERT INTO film_user_like (film_id, user_id) VALUES (?, ?) ", filmId, userId);
    }

    @Override
    public void remove(long filmId, long userId) {
        jdbcTemplate.update(" DELETE FROM film_user_like WHERE film_id = ? AND user_id = ? ", filmId, userId);

    }

    @Override
    public void clear() {
        jdbcTemplate.update(" DELETE FROM film_user_like ");

    }

    @Override
    public List<FilmUserLike> all() {
        return jdbcTemplate.query(" SELECT * FROM film_user_like ", filmUserLikeRowMapper);
    }

    @Override
    public List<Long> popularFilmIds(long limit) {
        String sql = " SELECT FILM_ID " +
                " FROM FILM_USER_LIKE " +
                " GROUP BY FILM_ID " +
                " ORDER BY COUNT(USER_ID) DESC " +
                " LIMIT ? ";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                rs.getLong("FILM_ID"), limit);
    }

    @Override
    public List<FilmUserLike> findUserLikeByFilmId(long filmId) {
        return jdbcTemplate.query(" SELECT * FROM film_user_like WHERE film_id = ?",
                filmUserLikeRowMapper, filmId);
    }

    @Override
    public List<FilmUserLike> findFilmLikeByUserId(long userId) {
        return jdbcTemplate.query(" SELECT * FROM film_user_like WHERE user_id = ?",
                filmUserLikeRowMapper, userId);
    }

    @Override
    public Set<Long> findUserLikedFilmIds(long userId) {
        String sql = "SELECT film_id FROM film_user_like WHERE user_id = ?";
        return new HashSet<>(jdbcTemplate.queryForList(sql, Long.class, userId));
    }

}
