package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenreStorage {

    void add(long filmId, long genreId);

    void deleteByFilmId(long filmId);

    List<FilmGenre> getAll();

    List<FilmGenre> findByFilmId(long filmId);

    List<Long> findFilmsIdByGenreId(Long genreId);

}
