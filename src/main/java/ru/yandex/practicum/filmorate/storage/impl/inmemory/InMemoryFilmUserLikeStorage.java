package ru.yandex.practicum.filmorate.storage.impl.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmUserLike;
import ru.yandex.practicum.filmorate.storage.FilmUserLikeStorage;

import java.util.*;

@Component
@Qualifier("inMemoryFilmUserLikeStorage")
public class InMemoryFilmUserLikeStorage implements FilmUserLikeStorage {
    private final HashMap<Long, Set<Long>> filmUserLike = new HashMap<>();

    @Override
    public void add(long filmId, long userId) {
        Set<Long> genre = filmUserLike.getOrDefault(filmId, Set.of(userId));
        genre.add(userId);
        filmUserLike.putIfAbsent(filmId, genre);
    }

    @Override
    public void remove(long filmId, long userId) {
        Set<Long> genre = filmUserLike.getOrDefault(filmId, Set.of());
        genre.remove(userId);
        filmUserLike.putIfAbsent(filmId, genre);
    }

    @Override
    public void clear() {
        filmUserLike.clear();
    }

    @Override
    public List<FilmUserLike> all() {
        List<FilmUserLike> filmUserLikeAll = new ArrayList<>();
        for (Map.Entry<Long, Set<Long>> filmGenreEntry : filmUserLike.entrySet()) {
            for (Long userId : filmGenreEntry.getValue()) {
                FilmUserLike filmUserLikeEntity = new FilmUserLike();
                filmUserLikeEntity.setFilmId(filmGenreEntry.getKey());
                filmUserLikeEntity.setUserId(userId);
                filmUserLikeAll.add(filmUserLikeEntity);
            }
        }
        return filmUserLikeAll;
    }

    @Override
    public List<Long> popularFilmIds(long limit) {
        return List.of();
    }

    @Override
    public List<Long> findPopularFilmsIdsFromList(List<Long> filmsIds, long limit) {
        return List.of();
    }

    @Override
    public List<FilmUserLike> findUserLikeByFilmId(long filmId) {
        List<FilmUserLike> filmUserLikeAll = new ArrayList<>();
        Set<Long> usersIds = filmUserLike.get(filmId);
        for (Long userId : usersIds) {
            FilmUserLike filmUserLikeEntity = new FilmUserLike();
            filmUserLikeEntity.setFilmId(filmId);
            filmUserLikeEntity.setUserId(userId);
            filmUserLikeAll.add(filmUserLikeEntity);
        }
        return filmUserLikeAll;
    }

    @Override
    public List<FilmUserLike> findFilmLikeByUserId(long userId) {
        List<FilmUserLike> filmUserLikeAll = new ArrayList<>();
        for (Map.Entry<Long, Set<Long>> filmGenreEntry : filmUserLike.entrySet()) {
            for (Long userIdIter : filmGenreEntry.getValue()) {
                if (userIdIter == userId) {
                    FilmUserLike filmUserLikeEntity = new FilmUserLike();
                    filmUserLikeEntity.setFilmId(filmGenreEntry.getKey());
                    filmUserLikeEntity.setUserId(userId);
                    filmUserLikeAll.add(filmUserLikeEntity);
                }
            }
        }
        return filmUserLikeAll;
    }

    @Override
    public Set<Long> findUserLikedFilmIds(long userId) {
        return null;
    }

    @Override
    public Set<Long> findUserIdsIntersectByFilmsLikesWithUserByUserId(long userId, Set<Long> filmIds) {
        return null;
    }

    @Override
    public void removeAllLikesByUserId(long userId) {

    }

    @Override
    public void removeAllLikesByFilmId(long filmId) {

    }
}
