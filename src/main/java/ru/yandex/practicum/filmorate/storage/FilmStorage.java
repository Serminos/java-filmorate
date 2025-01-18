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
<<<<<<< HEAD
=======

    List<Film> findFilmsByDirector(int directorId, String sortBy);
>>>>>>> 0e630ab4612c491009000b1fbb590c2e8a45baa4
}
