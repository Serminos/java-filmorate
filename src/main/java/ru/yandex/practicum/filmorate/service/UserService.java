package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.FilmUserLikeStorage;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;
    private final FilmStorage filmStorage;
    private final FilmUserLikeStorage filmUserLikeStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("friendshipDbStorage") FriendshipStorage friendshipStorage,
                       @Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("filmUserLikeDbStorage") FilmUserLikeStorage filmUserLikeStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
        this.filmStorage = filmStorage;
        this.filmUserLikeStorage = filmUserLikeStorage;
    }

    public UserDto create(UserDto user) {
        log.info("Creating user: {}", user);
        return UserMapper.mapToUserDto(userStorage.create(UserMapper.mapToUser(user)));
    }

    private void checkUserExists(long userId) {
        if (userStorage.findById(userId) == null) {
            throw new NotFoundException("User with ID - [" + userId + "] not found.");
        }
    }

    public UserDto update(UserDto user) {
        log.info("Updating user: {}", user);
        checkUserExists(user.getId());
        return UserMapper.mapToUserDto(userStorage.update(UserMapper.mapToUser(user)));
    }

    public List<UserDto> all() {
        log.info("Fetching all users");
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : userStorage.all()) {
            userDtos.add(UserMapper.mapToUserDto(user));
        }
        return userDtos;
    }

    public void clear() {
        log.info("Clearing all users and friendships");
        friendshipStorage.clear();
        userStorage.clear();
    }

    public void addFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new NotFoundException("Cannot add yourself as a friend.");
        }
        log.info("Adding friend: userId={}, friendId={}", userId, friendId);
        checkUserExists(userId);
        checkUserExists(friendId);
        Friendship friendship = new Friendship(userId, friendId, false);
        friendshipStorage.add(friendship);
    }

    public void deleteFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new NotFoundException("Cannot remove yourself as a friend.");
        }
        log.info("Deleting friend: userId={}, friendId={}", userId, friendId);
        checkUserExists(userId);
        checkUserExists(friendId);
        Friendship friendship = new Friendship(userId, friendId, false);
        friendshipStorage.remove(friendship);
    }

    public List<UserDto> commonFriends(long userId, long friendId) {
        log.info("Fetching common friends for userId={} and friendId={}", userId, friendId);
        checkUserExists(userId);
        checkUserExists(friendId);
        List<Long> commonFriendsIds = friendshipStorage.findCommonFriendId(userId, friendId);
        List<UserDto> commonFriends = new ArrayList<>();
        for (Long commonFriendId : commonFriendsIds) {
            commonFriends.add(UserMapper.mapToUserDto(userStorage.findById(commonFriendId)));
        }
        return commonFriends;
    }

    public List<UserDto> getFriends(long userId) {
        log.info("Fetching friends for userId={}", userId);
        checkUserExists(userId);
        List<UserDto> friends = new ArrayList<>();
        for (Friendship friendship : friendshipStorage.findAllByFromUserId(userId)) {
            friends.add(UserMapper.mapToUserDto(userStorage.findById(friendship.getToUserId())));
        }
        return friends;
    }

    public List<Film> getRecommendations(long userId) {
        log.info("Fetching recommendations for userId={}", userId);
        checkUserExists(userId);

        Set<Long> currentUserLikes = filmUserLikeStorage.findUserLikedFilmIds(userId);
        log.debug("User [{}] likes: {}", userId, currentUserLikes);

        List<Long> otherUsers = userStorage.all().stream()
                .map(User::getId)
                .filter(id -> id != userId)
                .toList();
        log.debug("Other users: {}", otherUsers);

        Long mostSimilarUserId = null;
        int maxCommonLikes = 0;

        for (Long otherUserId : otherUsers) {
            Set<Long> otherUserLikes = filmUserLikeStorage.findUserLikedFilmIds(otherUserId);
            int commonLikes = (int) currentUserLikes.stream()
                    .filter(otherUserLikes::contains)
                    .count();
            log.debug("Common likes with user [{}]: {}", otherUserId, commonLikes);

            if (commonLikes > maxCommonLikes) {
                maxCommonLikes = commonLikes;
                mostSimilarUserId = otherUserId;
            }
        }

        log.debug("Most similar user: {}", mostSimilarUserId);

        if (mostSimilarUserId == null) {
            return List.of();
        }

        Set<Long> similarUserLikes = filmUserLikeStorage.findUserLikedFilmIds(mostSimilarUserId);
        Set<Long> recommendedFilmIds = new HashSet<>(similarUserLikes);
        recommendedFilmIds.removeAll(currentUserLikes);

        log.debug("Recommended films (IDs): {}", recommendedFilmIds);

        return recommendedFilmIds.stream()
                .map(filmStorage::findById)
                .filter(Objects::nonNull)
                .toList();
    }
}
