package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Set;

public interface DirectorStorage {

    List<Director> getAll();

    Director findById(long directorId);

    Director create(Director director);

    Director update(Director newDirector);

    Integer deleteById(long directorId);

    Set<Director> findDirectorsByFilmId(long filmId);

    void checkExists(Set<Long> directorIdSet);

    Integer checkExistsById(long directorId);

    List<Director> findByNameContainingIgnoreCase(String query);
}