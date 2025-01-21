package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmUserLike;

import java.util.List;
import java.util.Set;

public interface FilmUserLikeStorage {

    void add(long filmId, long userId);

    void delete(long filmId, long userId);

    void clear();

    List<FilmUserLike> getAll();

    List<FilmUserLike> findByFilmId(long filmId);

    List<FilmUserLike> findByUserId(long userId);

    Set<Long> findFilmsIdByUserId(long userId);

    Set<Long> findUsersIdIntersectByFilmsLikesWithUserByUserId(long userId, Set<Long> filmsId);

    void deleteByUserId(long userId);

    void deleteByFilmId(long filmId);
}
