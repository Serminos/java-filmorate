package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class RatingMpa {
    Long ratingMpaId;
    @NotNull(message = "Рейтинг должен иметь название")
    String name;
    String description;
}
