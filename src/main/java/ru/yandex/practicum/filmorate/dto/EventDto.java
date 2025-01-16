package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    @NotNull(message = "ID события не может быть null.")
    private Long eventId;

    @NotNull(message = "ID пользователя не может быть null.")
    @Positive(message = "ID пользователя должен быть положительным числом.")
    private Long userId;

    @NotNull(message = "Timestamp не может быть null.")
    @Positive(message = "Timestamp должен быть положительным числом.")
    private Long timestamp;

    @NotNull(message = "Тип события не может быть null.")
    private EventType eventType;

    @NotNull(message = "Операция не может быть null.")
    private Operation operation;

    @NotNull(message = "ID сущности не может быть null.")
    @Positive(message = "ID сущности должен быть положительным числом.")
    private Long entityId;
}
