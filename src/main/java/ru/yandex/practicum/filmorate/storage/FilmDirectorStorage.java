package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.dto.DirectorDto;

import java.util.Set;

public interface FilmDirectorStorage {

        void createConnectionFilmDirectors(Long filmId, Set<DirectorDto> directors);

        void deleteConnectionFilmDirectorsByFilmId(Long filmId);

}
