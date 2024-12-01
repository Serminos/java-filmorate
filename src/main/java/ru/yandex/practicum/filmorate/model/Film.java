package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.annotations.MinDate;

import java.time.LocalDate;

/**
 * Film.
 */
@Getter
@Setter
@Builder
public class Film {
    long id;
    @NotNull(message = "Название фильма не может быть пустым;")
    @NotBlank(message = "Название фильма не может быть пустым;")
    String name;
    @Length(max = 200, message = "Максимальная длина описания фильма — 200 символов")
    String description;
    @MinDate(value = "1895-12-28", message = "Релиз не может быть раньше 28 декабря 1985 года.")
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    Long duration;
}
