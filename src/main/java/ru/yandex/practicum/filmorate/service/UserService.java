package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        if (userStorage.findById(user.getId()) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.update(user);
    }

    public Collection<User> all() {
        return userStorage.all();
    }

    public void clear() {
        userStorage.clear();
    }

    public void addFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new NotFoundException("Нельзя добавить себя в друзья");
        }
        userStorage.findById(userId).getFriendsIds().add(friendId);
        userStorage.findById(friendId).getFriendsIds().add(userId);
    }

    public void deleteFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new NotFoundException("Нельзя удалить себя из друзей");
        }
        userStorage.findById(userId).getFriendsIds().remove(friendId);
        userStorage.findById(friendId).getFriendsIds().remove(userId);
    }

    public List<User> commonFriends(long userId, long friendId) {
        List<User> commonFriends = new ArrayList<>();
        Set<Long> userFriends = userStorage.getFriendsIds(userId);
        Set<Long> friendFriends = userStorage.getFriendsIds(friendId);
        if (userFriends.isEmpty() || friendFriends.isEmpty()) {
            return commonFriends;
        }
        for (long userFriendId : userFriends) {
            if (friendFriends.contains(userFriendId)) {
                commonFriends.add(userStorage.findById(userFriendId));
            }
        }
        return commonFriends;
    }

    public List<User> getFriends(long userId) {
        List<User> friends = new ArrayList<>();
        for (long friendId : userStorage.getFriendsIds(userId)) {
            friends.add(userStorage.findById(friendId));
        }
        return friends;
    }
}
