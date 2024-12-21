package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;


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
    public UserDto create(@Valid @RequestBody UserDto user) {
        log.debug("Создание пользователя [{}]", user);
        return userService.create(user);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UserDto user) {
        log.debug("Обновление пользователя [{}]", user);
        return userService.update(user);
    }

    @GetMapping
    public List<UserDto> all() {
        return userService.all();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addUserFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteUserFriend(@PathVariable long id, @PathVariable long friendId) {
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
}
