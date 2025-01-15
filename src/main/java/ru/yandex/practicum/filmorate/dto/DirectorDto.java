package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DirectorDto {

    private Integer id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;
}
