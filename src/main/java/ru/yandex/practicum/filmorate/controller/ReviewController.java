package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;


@RestController
@RequestMapping(value = "/reviews")
@Validated
public class ReviewController {
    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ReviewDto create(@Valid @RequestBody ReviewDto reviewDto) {
        log.debug("Создание отзыва [{}]", reviewDto);
        return reviewService.create(reviewDto);
    }

    @PutMapping
    public ReviewDto update(@Valid @RequestBody ReviewDto reviewDto) {
        log.debug("Обновление отзыва [{}]", reviewDto);
        return reviewService.update(reviewDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.debug("Удаление отзыва [{}]", id);
        reviewService.deleteById(id);
    }

    @GetMapping("/{id}")
    public ReviewDto getById(@PathVariable Long id) {
        log.debug("Получение отзыва [{}]", id);
        return reviewService.findById(id);
    }

    @GetMapping
    public List<ReviewDto> getReviewsWithParam(@RequestParam(required = false) Long filmId,
                                               @RequestParam(defaultValue = "10") Long count) {
        if (filmId == null) {
            log.debug("Получение всех популярных отзывов в количестве [{}]", count);
            return reviewService.all(count);
        } else {
            log.debug("Получение всех популярных отзывов к фильму [{}] в количестве [{}]", filmId, count);
            return reviewService.findByFilmId(filmId, count);
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.debug("Добавление лайка к ревью [{}] - пользователем [{}]", id, userId);
        reviewService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.debug("Удаление лайка к ревью [{}] - пользователем [{}]", id, userId);
        reviewService.deleteLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable long id, @PathVariable long userId) {
        log.debug("Добавление дизлайка к ревью [{}] - пользователем [{}]", id, userId);
        reviewService.addDislike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable long id, @PathVariable long userId) {
        log.debug("Удаление дизлайка к ревью [{}] - пользователем [{}]", id, userId);
        reviewService.deleteDislike(id, userId);
    }
}
