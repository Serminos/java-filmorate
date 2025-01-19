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

    private static final String ADD = " INSERT INTO film_user_like (film_id, user_id) VALUES (?, ?) ";
    private static final String DELETE = " DELETE FROM film_user_like WHERE film_id = ? AND user_id = ? ";
    private static final String GET_ALL = " SELECT * FROM film_user_like ";
    private static final String FIND_USER_LIKE = " SELECT * FROM film_user_like WHERE film_id = ?";
    private static final String FIND_FILM_LIKE = " SELECT * FROM film_user_like WHERE user_id = ?";
    private static final String FIND_USER_LIKED_FILMS_IDS = "SELECT film_id FROM film_user_like WHERE user_id = ?";
    private static final String FIND_USER_IDS_INTERSECT_BY_FILMS_LIKES_WITH_USER = "SELECT user_id " +
            "FROM film_user_like WHERE user_id != ? " +
            "and film_id IN (?)";
    private static final String DELETE_ALL_LIKES_BY_USER_ID = "DELETE FROM film_user_like WHERE user_id = ?";
    private static final String DELETE_ALL_LIKES_BY_FILM_ID = "DELETE FROM film_user_like WHERE film_id = ?";
    private static final String FIND_POPULAR_FILM_IDS = " SELECT FILM_ID " +
            " FROM FILM_USER_LIKE " +
            " GROUP BY FILM_ID " +
            " ORDER BY COUNT(USER_ID) DESC " +
            " LIMIT ?;";


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
    public List<FilmUserLike> findUserLikeByFilmId(long filmId) {
        return jdbcTemplate.query(FIND_USER_LIKE, filmUserLikeRowMapper, filmId);
    }

    @Override
    public List<FilmUserLike> findFilmLikeByUserId(long userId) {
        return jdbcTemplate.query(FIND_FILM_LIKE, filmUserLikeRowMapper, userId);
    }

    @Override
    public Set<Long> findUserLikedFilmIdsByUserId(long userId) {
        return new HashSet<>(jdbcTemplate.queryForList(FIND_USER_LIKED_FILMS_IDS, Long.class, userId));
    }

    @Override
    public Set<Long> findUserIdsIntersectByFilmsLikesWithUserByUserId(long userId, Set<Long> filmIds) {
        if (filmIds.isEmpty()) {
            return new HashSet<>();
        }
        String inSql = String.join(",", Collections.nCopies(filmIds.size(), "?"));
        String sql = String.format(" SELECT user_id " +
                " FROM film_user_like WHERE user_id != %d " +
                " and film_id IN (%s) ", userId, inSql);
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), filmIds.toArray()));
    }

    @Override
    public void deleteAllLikesByUserId(long userId) {
        jdbcTemplate.update(DELETE_ALL_LIKES_BY_USER_ID, userId);
    }

    @Override
    public void deleteAllLikesByFilmId(long filmId) {
        jdbcTemplate.update(DELETE_ALL_LIKES_BY_FILM_ID, filmId);
    }

    @Override
    public List<Long> findPopularFilmIds(long limit) {
        return jdbcTemplate.query(FIND_POPULAR_FILM_IDS, (rs, rowNum) ->
                rs.getLong("FILM_ID"), limit);
    }

    @Override
    public List<Long> findPopularFilmsIdsFromList(List<Long> filmsIds, long limit) {
        if (filmsIds.isEmpty()) {
            return List.of();
        }

        String inSql = String.join(",", Collections.nCopies(filmsIds.size(), "?"));

        final String FIND_POPULAR_FILMS_IDS_FROM_LIST =
                "SELECT fl.film_id " +
                        "FROM film_user_like AS fl " +
                        "WHERE fl.film_id IN (" + inSql + ") " +
                        "GROUP BY fl.film_id " +
                        "ORDER BY COUNT(fl.user_id) DESC " +
                        "LIMIT ?;";

        List<Object> params = new ArrayList<>(filmsIds);
        params.add(limit);

        return jdbcTemplate.query(FIND_POPULAR_FILMS_IDS_FROM_LIST,
                (rs, rowNum) -> rs.getLong("film_id"),
                params.toArray());
    }
}
