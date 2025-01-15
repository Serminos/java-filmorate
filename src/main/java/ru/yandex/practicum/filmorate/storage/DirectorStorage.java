package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Set;

public interface DirectorStorage {

    List<Director> getAllDirectors();

    Director getDirectorById(int id);

    Director createDirector(Director director);

    Director updateDirector(Director newDirector);

    void deleteDirectorById(int id);

    Set<Director> getDirectorsByFilmId(long id);

    void checkDirectorsExist(Set<Integer> directorIdSet);

    void checkDirectorExistById(int directorId);
}