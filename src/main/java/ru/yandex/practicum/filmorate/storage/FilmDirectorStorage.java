package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.dto.DirectorDto;

import java.util.Set;

public interface FilmDirectorStorage {

        void create(Long id, Set<DirectorDto> directors);

        void deleteByFilmId(Long id);

}
