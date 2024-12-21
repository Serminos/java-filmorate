package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Long duration;
    Long ratingMpaId;
}
