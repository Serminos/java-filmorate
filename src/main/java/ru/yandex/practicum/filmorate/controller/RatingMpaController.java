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
    public List<MpaDto> getAllMpa() {
        return ratingMpaService.getAllMpa();
    }

    @GetMapping("/{id}")
    public MpaDto getMpaById(@PathVariable("id") int id) {
        return ratingMpaService.getMpaById(id);
    }
}
