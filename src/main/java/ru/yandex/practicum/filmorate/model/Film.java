package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import ru.yandex.practicum.filmorate.annotations.MinDate;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    long id;
    @NotBlank(message = "Название фильма не может быть пустым;")
    String name;
    @Size(max = 200, message = "Максимальная длина описания фильма — 200 символов")
    String description;
    @MinDate(value = "1895-12-28", message = "Релиз не может быть раньше 28 декабря 1895 года.")
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    Long duration;
}
