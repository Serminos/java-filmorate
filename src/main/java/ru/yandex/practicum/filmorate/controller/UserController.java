package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/users")
@Validated
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
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
        User user = (User) ex.getBindingResult().getTarget();
        String metodName = Objects.requireNonNull(ex.getParameter().getMethod()).getName();
        log.info("Ошибка валидации данных [{}] - [{}]", metodName, ex.getParameter().getParameterName());
        if (user != null) {
            log.info("Ошибка валидации данных [{}] - [{}]", metodName, user);
        }
        log.info("Ошибка валидации данных [{}] - [{}]", metodName, errors);
        return errors;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Создание пользователя [{}]", user);
        user.setId(getNextId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Обновление пользователя [{}]", user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            log.info("Не найден пользователь с id [{}]", user.getId());
            throw new NotFoundException("Пользователь не найден.");
        }
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
