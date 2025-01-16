package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DirectorDto {

    private Long id;

    @NotBlank(message = "Имя режиссера не может быть пустым")
    private String name;
}
