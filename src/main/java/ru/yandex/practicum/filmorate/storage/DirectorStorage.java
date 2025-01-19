package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Set;

public interface DirectorStorage {

    List<Director> getAll();

    Director findById(long directorId);

    Director create(Director director);

    Director update(Director director);

    Integer deleteByDirectorId(long directorId);

    Set<Director> findByFilmId(long filmId);

    void existsByDirectorIdIn(Set<Long> directorsId);

    Integer existsByDirectorId(long directorId);

    List<Director> findByNameContainingIgnoreCase(String query);
}