package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/films")
public class FilmController {
    private HashMap<Long, Film> films = new HashMap();
    private static final LocalDate START_FILMS = LocalDate.of(1895, 12, 28);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            Film oldFilm = films.get(film.getId());
            if (film.getName() == null) {
                film.setName(oldFilm.getName());
            }
            if (film.getDescription() == null) {
                film.setDescription(oldFilm.getDescription());
            }
            if (film.getDuration() == null || film.getDuration() == 0) {
                film.setDuration(oldFilm.getDuration());
            }
            if (film.getReleaseDate() == null) {
                film.setReleaseDate(oldFilm.getReleaseDate());
            }
            films.put(film.getId(), film);
            return film;
        } else {
            throw new NotFoundException("Фильм не найден.");
        }
    }

    @GetMapping
    public Collection<Film> all() {
        return films.values();
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

    public void clear() {
        films.clear();
    }
}
