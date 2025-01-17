package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenreStorage {
    void addGenre(long filmId, long genreId);

    void removeGenreByFilmId(long filmId);

    void removeGenreByFilmIdAndGenreId(long filmId, long genreId);

    List<FilmGenre> all();

    List<FilmGenre> findGenreByFilmId(long filmId);

    List<FilmGenre> findFilmByGenreId(long genreId);

    List<Long> findFilmsIdsByGenreId(Long genreId);
}
