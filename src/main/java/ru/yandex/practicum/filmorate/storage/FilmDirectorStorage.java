package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.FilmDirector;

import java.util.List;
import java.util.Set;

public interface FilmDirectorStorage {

    void create(Long filmId, Set<DirectorDto> directors);

    void deleteByFilmId(Long filmId);

    List<FilmDirector> findByDirectorIdIn(List<Long> directorIds);

}
