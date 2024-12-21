package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("friendshipDbStorage") FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public UserDto create(UserDto user) {
        return UserMapper.mapToUserDto(userStorage.create(UserMapper.mapToUser(user)));
    }

    public UserDto update(UserDto user) {
        if (userStorage.findById(user.getId()) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return UserMapper.mapToUserDto(userStorage.update(UserMapper.mapToUser(user)));
    }

    public List<UserDto> all() {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : userStorage.all()) {
            userDtos.add(UserMapper.mapToUserDto(user));
        }
        return userDtos;
    }

    public void clear() {
        friendshipStorage.clear();
        userStorage.clear();
    }

    private void checkUsers(long userId, long friendId) {
        if (userStorage.findById(userId) == null) {
            throw new NotFoundException("Не найден пользователь с ID - [" + userId + "]");
        }
        if (userStorage.findById(friendId) == null) {
            throw new NotFoundException("Не найден пользователь с ID - [" + friendId + "]");
        }
    }

    public void addFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new NotFoundException("Нельзя добавить себя в друзья");
        }
        checkUsers(userId, friendId);
        Friendship friendship = new Friendship(userId, friendId, false);
        friendshipStorage.add(friendship);
    }

    public void deleteFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new NotFoundException("Нельзя удалить себя из друзей");
        }
        checkUsers(userId, friendId);
        Friendship friendship = new Friendship(userId, friendId, false);
        friendshipStorage.remove(friendship);
    }

    public List<UserDto> commonFriends(long userId, long friendId) {
        checkUsers(userId, friendId);
        List<Long> commonFriendsIds = friendshipStorage.findCommonFriendId(userId, friendId);
        List<UserDto> commonFriends = new ArrayList<>();
        for (Long commonFriendId : commonFriendsIds) {
            commonFriends.add(UserMapper.mapToUserDto(userStorage.findById(commonFriendId)));
        }
        return commonFriends;
    }

    public List<UserDto> getFriends(long userId) {
        if (userStorage.findById(userId) == null) {
            throw new NotFoundException("Не найден пользователь с ID - [" + userId + "]");
        }
        List<UserDto> friends = new ArrayList<>();
        for (Friendship friendship : friendshipStorage.findAllByFromUserId(userId)) {
            friends.add(UserMapper.mapToUserDto(userStorage.findById(friendship.getToUserId())));
        }
        return friends;
    }
}
