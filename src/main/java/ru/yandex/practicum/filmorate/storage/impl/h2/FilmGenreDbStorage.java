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

    @Override
    public void addGenre(long filmId, long genreId) {
        jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?) ", filmId, genreId);
    }

    @Override
    public void removeGenreByFilmId(long filmId) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ? ", filmId);
    }

    @Override
    public void removeGenreByFilmIdAndGenreId(long filmId, long genreId) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ? AND genre_id = ? ", filmId, genreId);
    }

    @Override
    public List<FilmGenre> all() {
        return jdbcTemplate.query(" SELECT * FROM film_genre ", filmGenreRowMapper);
    }

    @Override
    public List<FilmGenre> findGenreByFilmId(long filmId) {
        return jdbcTemplate.query("SELECT * FROM film_genre WHERE film_id = ?",
                filmGenreRowMapper, filmId);
    }

    @Override
    public List<FilmGenre> findFilmByGenreId(long genreId) {
        return jdbcTemplate.query("SELECT * FROM film_genre WHERE genre_id = ?",
                filmGenreRowMapper, genreId);
    }
}
