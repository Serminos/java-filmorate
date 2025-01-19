package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage {
    void add(Friendship friendship);

    void update(Friendship friendship);

    void delete(Friendship friendship);

    void clear();

    List<Friendship> getAll();

    List<Friendship> findAllByFromUserId(long fromUserId);

    List<Friendship> findAllByFromUserIdAndConfirmedStatus(long fromUserId, boolean isConfirmed);

    List<Friendship> findAllByToUserId(long toUserId);

    List<Friendship> findAllByToUserIdAndConfirmedStatus(long toUserId, boolean isConfirmed);

    List<Long> findCommonFriendId(long userId, long otherId);

    void deleteAllByUserId(long userId);
}
