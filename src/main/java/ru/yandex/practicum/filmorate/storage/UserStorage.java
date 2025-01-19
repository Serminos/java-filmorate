package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User findByUserId(long userId);

    List<User> getAll();

    void deleteByUserId(long userId);

    void clear();
}
