package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<DirectorDto> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @GetMapping("/{id}")
    public DirectorDto getDirectorById(@PathVariable("id") int id) {
        return directorService.getDirectorById(id);
    }

    @PostMapping
    public DirectorDto createDirector(@Valid @RequestBody DirectorDto directorDto) {
        return directorService.createDirector(directorDto);
    }

    @PutMapping
    public DirectorDto updateDirector(@Valid @RequestBody DirectorDto newDirectorDto) {
        return directorService.updateDirector(newDirectorDto);
    }

    @DeleteMapping("/{id}")
    public void deleteDirectorById(@PathVariable("id") int id) {
        directorService.deleteDirectorById(id);
    }
}
