package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> all() {
        return filmStorage.all();
    }

    public void addLike(long filmId, long userId) {
        userStorage.findById(userId);
        filmStorage.findById(filmId).getLikes().add(userId);
    }

    public void deleteLike(long filmId, long userId) {
        boolean deleted = filmStorage.deleteLikeByUserId(filmId, userId);
    }

    public void clear() {
        filmStorage.clear();
    }

    public List<Film> findPopularFilms(long count) {
        return filmStorage.all().stream()
                .sorted((film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
