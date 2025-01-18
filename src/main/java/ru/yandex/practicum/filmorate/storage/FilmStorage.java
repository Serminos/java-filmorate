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

    List<Film> findFilmsByDirector(int directorId, String sortBy);

    List<Film> findByNameContainingIgnoreCase(String query);

    List<Film> findPopularByFilmIdIn(List<Long> ids);
    List<Long> findFilmsIdsByYear(Integer year);
}
