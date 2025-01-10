package ru.yandex.practicum.filmorate.service.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenreMapper {

    public static GenreDto mapToGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getGenreId());
        genreDto.setName(genre.getName());
        return genreDto;
    }

    public static Genre mapToGenre(GenreDto genreDto) {
        Genre genre = new Genre();
        genre.setGenreId(genreDto.getId());
        genre.setName(genreDto.getName());
        return genre;
    }
}
