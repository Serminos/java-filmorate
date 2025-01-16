package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.mapper.FilmMapper;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/users")
@Validated
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        log.debug("Создание пользователя [{}]", userDto);
        return userService.create(userDto);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UserDto userDto) {
        log.debug("Обновление пользователя [{}]", userDto);
        return userService.update(userDto);
    }

    @GetMapping
    public List<UserDto> all() {
        return userService.all();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addUserFriend(@PathVariable long id, @PathVariable long friendId) {
        log.debug("Добавление друзей [{}] - [{}]", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteUserFriend(@PathVariable long id, @PathVariable long friendId) {
        log.debug("Удаление друзей [{}] - [{}]", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<UserDto> getUserFriends(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public List<UserDto> commonFriends(@PathVariable long id, @PathVariable long friendId) {
        return userService.commonFriends(id, friendId);
    }

    @GetMapping("/{id}/recommendations")
    public List<FilmDto> getRecommendations(@PathVariable long id) {
        log.debug("Получен запрос на получение рекомендаций для пользователя [{}]", id);
        List<Film> recommendations = userService.getRecommendations(id);
        List<FilmDto> recommendationDtos = recommendations.stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
        log.debug("Сформированы рекомендации для пользователя [{}]: {}", id, recommendationDtos);
        return recommendationDtos;
      
    @GetMapping("/{id}/feed")
    public List<EventDto> getEvent(@PathVariable long id) {
        log.debug("Получение ленты событий пользователя - [{}]", id);
        return userService.getUserEvent(id);
    }
}
