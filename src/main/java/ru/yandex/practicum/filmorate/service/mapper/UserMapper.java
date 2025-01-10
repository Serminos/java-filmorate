package ru.yandex.practicum.filmorate.service.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setName(user.getName() == null ? user.getLogin() : user.getName());
        userDto.setBirthday(user.getBirthday());
        return userDto;
    }

    public static User mapToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setLogin(userDto.getLogin());
        user.setName(userDto.getName() == null ? userDto.getLogin() : userDto.getName());
        user.setBirthday(userDto.getBirthday());
        return user;
    }
}
