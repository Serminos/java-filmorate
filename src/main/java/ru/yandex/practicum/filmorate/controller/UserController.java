package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequestMapping(value = "/users")
@Validated
public class UserController {
    private final HashMap<Long, User> users = new HashMap();

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
    public User create(@Valid @RequestBody User user) {
        user.setId(getNextId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            User oldUser = users.get(user.getId());
            if (user.getName() == null) {
                user.setName(oldUser.getName());
            }
            if (user.getLogin() == null) {
                user.setLogin(oldUser.getLogin());
            }
            if (user.getEmail() == null) {
                user.setEmail(oldUser.getEmail());
            }
            if (user.getBirthday() == null) {
                user.setBirthday(oldUser.getBirthday());
            }
            users.remove(user.getId());
            users.put(user.getId(), user);
            return user;
        } else throw new NotFoundException("Пользователь не найден.");
    }

    @GetMapping
    public Collection<User> all() {
        Collection<User> results = users.values().stream()
                .map(e -> User.builder()
                        .id(e.getId())
                        .email(e.getEmail())
                        .login(e.getLogin())
                        .name(e.getName().isBlank() ? e.getEmail() : e.getName())
                        .birthday(e.getBirthday())
                        .build()
                ).collect(Collectors.toCollection(ArrayList::new));
        return users.values();
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public void clear() {
        users.clear();
    }
}
