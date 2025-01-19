package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.impl.h2.mappers.FilmGenreRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Qualifier("filmGenreDbStorage")
class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreRowMapper filmGenreRowMapper;

    private static final String ADD = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?) ";
    private static final String DELETE_GENRE_BY_FILM_ID = "DELETE FROM film_genre WHERE film_id = ? ";
    private static final String DELETE_GENRE_BY_FILM_ID_AND_GENRE_ID = "DELETE FROM film_genre " +
                                                                     "WHERE film_id = ? AND genre_id = ? ";
    private static final String GET_ALL = " SELECT * FROM film_genre ";
    private static final String FIND_GENRE_BY_FILM_ID = "SELECT * FROM film_genre WHERE film_id = ?";
    private static final String FIND_FILM_BY_GENRE_ID = "SELECT * FROM film_genre WHERE genre_id = ?";
    private static final String FIND_FILM_IDS_BY_GENRE_ID = "SELECT film_id FROM film_genre WHERE genre_id = ?;";

    @Override
    public void add(long filmId, long genreId) {
        jdbcTemplate.update(ADD, filmId, genreId);
    }

    @Override
    public void deleteGenreByFilmId(long filmId) {
        jdbcTemplate.update(DELETE_GENRE_BY_FILM_ID, filmId);
    }

    @Override
    public void deleteGenreByFilmIdAndGenreId(long filmId, long genreId) {
        jdbcTemplate.update(DELETE_GENRE_BY_FILM_ID_AND_GENRE_ID, filmId, genreId);
    }

    @Override
    public List<FilmGenre> getAll() {
        return jdbcTemplate.query(GET_ALL, filmGenreRowMapper);
    }

    @Override
    public List<FilmGenre> findGenreByFilmId(long filmId) {
        return jdbcTemplate.query(FIND_GENRE_BY_FILM_ID, filmGenreRowMapper, filmId);
    }

    @Override
    public List<FilmGenre> findFilmByGenreId(long genreId) {
        return jdbcTemplate.query(FIND_FILM_BY_GENRE_ID, filmGenreRowMapper, genreId);
    }

    @Override
    public List<Long> findFilmIdsByGenreId(Long genreId) {
        return jdbcTemplate.query(FIND_FILM_IDS_BY_GENRE_ID, (rs, rowNum) ->
                rs.getLong("film_id"), genreId);
    }
}
