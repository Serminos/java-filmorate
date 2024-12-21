package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmUserLike {
    Long filmId;
    Long userId;
}
