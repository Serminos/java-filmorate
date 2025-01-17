package ru.yandex.practicum.filmorate.storage.impl.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Component
@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> users = new HashMap<>();

    public User create(User user) {
        user.setId(getNextId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    @Override
    public User findById(long userId) {
        if (users.isEmpty() || !users.containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return users.get(userId);
    }

    public Collection<User> all() {
        Collection<User> results = users.values().stream()
                .map(e -> User.builder()
                        .id(e.getId())
                        .email(e.getEmail())
                        .login(e.getLogin())
                        .name(e.getName().isBlank() ? e.getEmail() : e.getName())
                        .birthday(e.getBirthday())
                        .build()
                ).collect(Collectors.toCollection(ArrayList::new));
        return results;
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public void deleteUser(long id) {
        users.remove(id);
    }

    public void clear() {
        users.clear();
    }
}
