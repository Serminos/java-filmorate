package ru.yandex.practicum.filmorate.storage.impl.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("inMemoryFriendshipStorage")
public class InMemoryFriendshipStorage implements FriendshipStorage {
    private final HashMap<Long, Map<Long, Boolean>> friendshipStorage = new HashMap<>();

    @Override
    public void add(Friendship friendship) {
        if (friendshipStorage.get(friendship.getFromUserId()) == null) {
            friendshipStorage.put(friendship.getFromUserId(),
                    Map.of(friendship.getToUserId(), friendship.isConfirmed()));
        } else {
            friendshipStorage.get(friendship.getFromUserId())
                    .put(friendship.getToUserId(), friendship.isConfirmed());
        }
    }

    @Override
    public void update(Friendship friendship) {
        if (friendshipStorage.get(friendship.getFromUserId()) == null) {
            friendshipStorage.put(friendship.getFromUserId(),
                    Map.of(friendship.getToUserId(), friendship.isConfirmed()));
        } else {
            friendshipStorage.get(friendship.getFromUserId())
                    .put(friendship.getToUserId(), friendship.isConfirmed());
        }
    }

    @Override
    public void remove(Friendship friendship) {
        if (friendshipStorage.get(friendship.getFromUserId()) != null &&
                friendshipStorage.get(friendship.getFromUserId()).get(friendship.getToUserId()) != null) {
            friendshipStorage.get(friendship.getFromUserId()).remove(friendship.getToUserId());
        }
    }

    @Override
    public void clear() {
        friendshipStorage.clear();
    }

    @Override
    public List<Friendship> all() {
        List<Friendship> friendships = new ArrayList<>();
        for (Map.Entry<Long, Map<Long, Boolean>> friendshipsEntry : friendshipStorage.entrySet()) {
            for (Map.Entry<Long, Boolean> friendshipEntry : friendshipsEntry.getValue().entrySet()) {
                Friendship friendship = new Friendship();
                friendship.setFromUserId(friendshipsEntry.getKey());
                friendship.setToUserId(friendshipEntry.getKey());
                friendship.setConfirmed(friendshipEntry.getValue());
                friendships.add(friendship);
            }
        }
        return friendships;
    }

    @Override
    public List<Friendship> findAllByFromUserId(long fromUserId) {
        List<Friendship> friendships = new ArrayList<>();
        for (Map.Entry<Long, Map<Long, Boolean>> friendshipsEntry : friendshipStorage.entrySet()) {
            if (friendshipsEntry.getKey() != fromUserId) continue;
            for (Map.Entry<Long, Boolean> friendshipEntry : friendshipsEntry.getValue().entrySet()) {
                Friendship friendship = new Friendship();
                friendship.setFromUserId(friendshipsEntry.getKey());
                friendship.setToUserId(friendshipEntry.getKey());
                friendship.setConfirmed(friendshipEntry.getValue());
                friendships.add(friendship);
            }
        }
        return friendships;
    }

    @Override
    public List<Friendship> findAllByFromUserIdAndConfirmedStatus(long fromUserId, boolean isConfirmed) {
        List<Friendship> friendships = new ArrayList<>();
        for (Map.Entry<Long, Map<Long, Boolean>> friendshipsEntry : friendshipStorage.entrySet()) {
            if (friendshipsEntry.getKey() != fromUserId) continue;
            for (Map.Entry<Long, Boolean> friendshipEntry : friendshipsEntry.getValue().entrySet()) {
                Friendship friendship = new Friendship();
                friendship.setFromUserId(friendshipsEntry.getKey());
                friendship.setToUserId(friendshipEntry.getKey());
                if (friendshipEntry.getValue() != isConfirmed) continue;
                friendship.setConfirmed(friendshipEntry.getValue());
                friendships.add(friendship);
            }
        }
        return friendships;
    }

    @Override
    public List<Friendship> findAllByToUserId(long toUserId) {
        List<Friendship> friendships = new ArrayList<>();
        for (Map.Entry<Long, Map<Long, Boolean>> friendshipsEntry : friendshipStorage.entrySet()) {
            for (Map.Entry<Long, Boolean> friendshipEntry : friendshipsEntry.getValue().entrySet()) {
                Friendship friendship = new Friendship();
                friendship.setFromUserId(friendshipsEntry.getKey());
                if (friendshipEntry.getKey() != toUserId) continue;
                friendship.setToUserId(friendshipEntry.getKey());
                friendship.setConfirmed(friendshipEntry.getValue());
                friendships.add(friendship);
            }
        }
        return friendships;
    }

    @Override
    public List<Friendship> findAllByToUserIdAndConfirmedStatus(long toUserId, boolean isConfirmed) {
        List<Friendship> friendships = new ArrayList<>();
        for (Map.Entry<Long, Map<Long, Boolean>> friendshipsEntry : friendshipStorage.entrySet()) {
            for (Map.Entry<Long, Boolean> friendshipEntry : friendshipsEntry.getValue().entrySet()) {
                Friendship friendship = new Friendship();
                friendship.setFromUserId(friendshipsEntry.getKey());
                if (friendshipEntry.getKey() != toUserId) continue;
                friendship.setToUserId(friendshipEntry.getKey());
                if (friendshipEntry.getValue() != isConfirmed) continue;
                friendship.setConfirmed(friendshipEntry.getValue());
                friendships.add(friendship);
            }
        }
        return friendships;
    }

    @Override
    public List<Long> findCommonFriendId(long userId, long otherId) {
        List<Friendship> friendshipUser = findAllByFromUserId(userId);
        List<Friendship> friendshipOtherUser = findAllByFromUserId(otherId);
        List<Long> coommonFriendId = new ArrayList<>();
        for (Friendship friendship1 : friendshipUser) {
            for (Friendship friendship2 : friendshipOtherUser) {
                if (friendship2.getToUserId().equals(friendship1.getToUserId())) {
                    coommonFriendId.add(friendship1.getToUserId());
                }
            }
        }
        return coommonFriendId;
    }

    @Override
    public void removeAllByUserId(long userId) {
        friendshipStorage.remove(userId);

        friendshipStorage.forEach((fromUserId, friends) -> friends.remove(userId));
    }
}
