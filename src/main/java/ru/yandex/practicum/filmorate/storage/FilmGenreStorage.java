package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenreStorage {

    void add(long filmId, long genreId);

    void deleteGenreByFilmId(long filmId);

    void deleteGenreByFilmIdAndGenreId(long filmId, long genreId);

    List<FilmGenre> getAll();

    List<FilmGenre> findGenreByFilmId(long filmId);

    List<FilmGenre> findFilmByGenreId(long genreId);

    List<Long> findFilmIdsByGenreId(Long genreId);

}
