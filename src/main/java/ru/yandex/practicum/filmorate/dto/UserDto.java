package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Validated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    Long id;
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    @NotNull(message = "Электронная почта не может быть пустой и должна содержать символ @")
    String email;
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    @Pattern(regexp = "[^ ]+", message = "Логин не может содержать пробелы")
    String login;
    String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;
    Set<Long> friends = new HashSet<>();

    public UserDto(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }

    public void setFriendsIds(Set<Long> friendsIds) {
        this.friends = friendsIds != null ? friendsIds : new HashSet<>();
    }
}
