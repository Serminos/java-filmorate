package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmUserLike;

import java.util.List;

public interface FilmUserLikeStorage {
    void add(long filmId, long userId);

    void remove(long filmId, long userId);

    void clear();

    List<FilmUserLike> all();

    List<FilmUserLike> findUserLikeByFilmId(long filmId);

    List<FilmUserLike> findFilmLikeByUserId(long userId);
}
