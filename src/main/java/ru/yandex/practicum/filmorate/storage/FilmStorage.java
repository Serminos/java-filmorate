package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    List<Film> all();

    Film findById(long filmId);

    boolean deleteLikeByUserId(long filmId, long userId);

    void clear();

    List<Film> findByIds(List<Long> ids);

    List<Film> findFilmsByDirector(long directorId, String sortBy);

    List<Film> findByNameContainingIgnoreCase(String query);

    List<Film> findPopularByFilmIdIn(List<Long> ids);

    void deleteFilm(long filmId);

    List<Film> findPopular(long limit);


    List<Long> findFilmsIdsByYear(int year);
}
