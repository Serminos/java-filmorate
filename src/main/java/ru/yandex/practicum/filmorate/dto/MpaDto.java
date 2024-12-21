package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MpaDto {
    Long id;
    @NotNull(message = "Рейтинг должен иметь название")
    String name;
    String description;
}
