package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody FilmDto filmDto) {
        log.debug("Создание фильма [{}]", filmDto);
        filmDto = filmService.create(filmDto);
        return filmDto;
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody FilmDto filmDto) {
        log.debug("Обновление фильма [{}]", filmDto);
        filmDto = filmService.update(filmDto);
        return filmDto;
    }

    @GetMapping
    public List<FilmDto> all() {
        return filmService.all();
    }

    @GetMapping("/{id}")
    public FilmDto getFilmById(@PathVariable long id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.debug("Добавление лайка для фильма [{}] пользователем [{}]", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.debug("Удаление лайка для фильма [{}] пользователем [{}]", id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<FilmDto> getPopularFilmsByParams(@RequestParam(defaultValue = "10") Long count,
                                                 @RequestParam(required = false) Long genreId,
                                                 @RequestParam(required = false) Long year) {
        log.debug("Получен запрос на получение самых популярных фильмов в количестве = [{}], " +
                "возможна фильтрация по параметрам: жанру с id [{}] и/или году [{}]", count, genreId, year);
        Map<String, Long> params = new HashMap<>();
        Optional.ofNullable(genreId).ifPresent(v -> params.put("genreId", genreId));
        Optional.ofNullable(year).ifPresent(v -> params.put("year", year));

        return filmService.getPopularFilmsByParams(params, count);
    }

    @GetMapping("/common")
    public List<FilmDto> getCommonFilms(@RequestParam long userId, @RequestParam long friendId) {
        log.debug("Получен запрос на общие фильмы для пользователей: userId=[{}], friendId=[{}]", userId, friendId);
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<FilmDto> getFilmsByDirectorIdWithSort(@PathVariable long directorId, @RequestParam String sortBy) {
        log.debug("Получен запрос на получение всех фильмов режиссера с directorId = [{}], сортировка по [{}]",
                directorId, sortBy);
        return filmService.getFilmsByDirectorIdWithSort(directorId, sortBy);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable long id) {
        log.debug("Удаление фильма с идентификатором [{}]", id);
        filmService.deleteFilm(id);
    }

    @GetMapping("/search")
    public List<FilmDto> getSearch(@RequestParam String query, @RequestParam List<String> by) {
        log.debug("Получен запрос на поиск фильма: query=[{}], by=[{}]", query, by);
        return filmService.getSearch(query, by);
    }
}
