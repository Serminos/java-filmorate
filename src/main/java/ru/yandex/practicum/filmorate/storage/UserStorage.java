package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User findById(long userId);

    Collection<User> all();

    void clear();

    void deleteUser(long id);
}
