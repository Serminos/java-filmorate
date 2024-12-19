package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.MinDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    Long id;
    @NotBlank(message = "Название фильма не может быть пустым;")
    String name;
    @Size(max = 200, message = "Максимальная длина описания фильма — 200 символов")
    String description;
    @MinDate(value = "1895-12-28", message = "Релиз не может быть раньше 28 декабря 1895 года.")
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    Long duration;
    Set<Long> likes = new HashSet<>();
    Set<Genre> genre = new HashSet<>();
    RatingMpa ratingMpa;

    public Film(Long id, String name, String description, LocalDate releaseDate, Long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
    }

    public void setLikes(Set<Long> likes) {
        this.likes = likes != null ? likes : new HashSet<>();
    }
}
