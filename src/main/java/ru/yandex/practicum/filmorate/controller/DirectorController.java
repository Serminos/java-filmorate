package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<DirectorDto> getAll() {
        log.debug("Получен запрос на получение списка всех режиссеров");
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    public DirectorDto getById(@PathVariable("id") long id) {
        log.debug("Получен запрос на поиск режиссера по его id: [{}]", id);
        return directorService.getById(id);
    }

    @PostMapping
    public DirectorDto create(@Valid @RequestBody DirectorDto directorDto) {
        log.debug("Получен запрос на добавление режиссера: [{}]", directorDto);
        return directorService.create(directorDto);
    }

    @PutMapping
    public DirectorDto update(@Valid @RequestBody DirectorDto newDirectorDto) {
        log.debug("Получен запрос на обновление режиссера: [{}]", newDirectorDto);
        return directorService.update(newDirectorDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") long id) {
        log.debug("Получен запрос на удаление режиссера с id: [{}]", id);
        directorService.deleteById(id);
    }
}
