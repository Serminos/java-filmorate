package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Set;

public interface DirectorStorage {

    List<Director> getAllDirectors();

    Director findDirectorById(long id);

    Director createDirector(Director director);

    Director updateDirector(Director newDirector);

    Integer deleteDirectorById(long id);

    Set<Director> getDirectorsByFilmId(long id);

    void checkExists(Set<Long> directorIdSet);

    Integer checkExistsById(long id);

    List<Director> findByNameContainingIgnoreCase(String query);
}