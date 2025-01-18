package ru.yandex.practicum.filmorate.storage.impl.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Qualifier("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();

    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new NotFoundException("Фильм не найден.");
        }
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public List<Film> all() {
        return films.values().stream().toList();
    }

    public Film findById(long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        return films.get(id);
    }

    public boolean deleteLikeByUserId(long filmId, long userId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Фильм не найден");
        }
        // TODO REFACTOR
        /*if (!films.get(filmId).getUserLikeIds().remove(userId)) {
            throw new NotFoundException("Для указанного фильма - лайк не найден");
        }*/
        return true;
    }

    public void clear() {
        films.clear();
    }

    @Override
    public List<Film> findByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        return ids.stream()
                .map(films::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> findFilmsByDirector(int directorId, String sortBy) {
        return List.of();
    }

    @Override
    public List<Film> findByNameContainingIgnoreCase(String query) {
        return null;
    }

    @Override
    public List<Film> findPopularByFilmIdIn(List<Long> ids) {
        return null;
    }
}
