package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.enums.SortBy;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    List<Film> getAll();

    Film findByFilmId(long filmId);

    void clear();

    List<Film> findByFilmIdIn(List<Long> filmsId);

    List<Film> findByDirectorIdWithSort(long directorId, SortBy sortBy);

    List<Film> findByNameContainingIgnoreCase(String query);

    List<Film> findPopularByFilmIdIn(List<Long> filmsId, long limit);

    void deleteByFilmId(long filmId);

    List<Long> findPopularFilmsIdWithLimit(long limit);

    List<Long> findFilmsIdByYear(int year);
}
