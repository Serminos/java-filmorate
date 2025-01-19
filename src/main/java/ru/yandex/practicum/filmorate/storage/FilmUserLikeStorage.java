package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmUserLike;

import java.util.List;
import java.util.Set;

public interface FilmUserLikeStorage {

    void add(long filmId, long userId);

    void delete(long filmId, long userId);

    void clear();

    List<FilmUserLike> getAll();

    List<FilmUserLike> findUserLikeByFilmId(long filmId);

    List<FilmUserLike> findFilmLikeByUserId(long userId);

    Set<Long> findUserLikedFilmIdsByUserId(long userId);

    Set<Long> findUserIdsIntersectByFilmsLikesWithUserByUserId(long userId, Set<Long> filmIds);

    void deleteAllLikesByUserId(long userId);

    void deleteAllLikesByFilmId(long filmId);

    List<Long> findPopularFilmIds(long limit);

    List<Long> findPopularFilmsIdsFromList(List<Long> filmsIds, long limit);

}
