package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
@Data
@Builder
public class User {
    long id;

    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    @NotNull(message = "Электронная почта не может быть пустой и должна содержать символ @")
    String email;

    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    @Pattern(regexp = "[^ ]+", message = "Логин не может содержать пробелы")
    String login;

    String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;
}
