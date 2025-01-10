package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/mpa")
public class RatingMpaController {
    private final RatingMpaService ratingMpaService;

    @GetMapping
    public List<MpaDto> findAllMpa() {
        return ratingMpaService.findAllRatingMpa();
    }

    @GetMapping("/{id}")
    public MpaDto findMpaById(@PathVariable("id") int id) {
        return ratingMpaService.findRatingMpaById(id);
    }
}
