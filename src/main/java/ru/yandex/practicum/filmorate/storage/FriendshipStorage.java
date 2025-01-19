package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage {
    void add(Friendship friendship);

    void update(Friendship friendship);

    void delete(Friendship friendship);

    void clear();

    List<Friendship> getAll();

    List<Friendship> findByFromUserId(long fromUserId);

    List<Long> findCommonFriendId(long userId, long otherId);

    void deleteByUserId(long userId);
}
