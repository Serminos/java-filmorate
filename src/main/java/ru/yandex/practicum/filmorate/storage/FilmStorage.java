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

    List<Film> findFilmsLikedByUser(long userId);

    int countLikes(long filmId);


}
