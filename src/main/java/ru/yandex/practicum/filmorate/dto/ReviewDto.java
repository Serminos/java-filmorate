package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    Long reviewId;
    @NotBlank(message = "Отзыв не может быть пустым")
    String content;
    Boolean isPositive;
    @NotNull(message = "ID пользователя не указан")
    Long userId;
    @NotNull(message = "ID фильма не указан")
    Long filmId;
    Integer useful;
}
