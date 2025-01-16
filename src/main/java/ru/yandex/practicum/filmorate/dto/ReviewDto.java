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
    private Long reviewId;
    @NotBlank(message = "Отзыв не может быть пустым")
    private String content;
    @NotNull(message = "Отзыв должен иметь тип - Положительный или Отрицательный")
    private Boolean isPositive;
    @NotNull(message = "ID пользователя не указан")
    private Long userId;
    @NotNull(message = "ID фильма не указан")
    private Long filmId;
    private Integer useful;
}
