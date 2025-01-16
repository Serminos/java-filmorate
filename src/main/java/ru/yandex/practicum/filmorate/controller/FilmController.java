package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

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
    public List<FilmDto> getPopularFilmsByGenreAndYearWithLimit(@RequestParam(defaultValue = "10") Long count,
                                                                @RequestParam(required = false) Long genreId,
                                                                @RequestParam(required = false) Integer year) {
        log.debug("Получен запрос на получение самых популярных фильмов в количестве = [{}], " +
                "фильтрация по жанру с id [{}] и году [{}]", count, genreId, year);
        return filmService.getPopularFilmsByGenreAndYearWithLimit(count, genreId, year);
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
}