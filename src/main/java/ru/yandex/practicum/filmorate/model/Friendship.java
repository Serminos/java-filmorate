package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friendship {
    Long fromUserId;
    Long toUserId;
    boolean isConfirmed = false;
}
