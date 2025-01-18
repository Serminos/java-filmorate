package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.service.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.service.mapper.RatingMpaMapper;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final FilmUserLikeStorage filmUserLikeStorage;
    private final EventStorage eventStorage;
    private final Map<Long, RatingMpa> cacheRatingMpa;
    private final Map<Long, Genre> cacheGenre;
    private final DirectorStorage directorStorage;
    private final FilmDirectorStorage filmDirectorStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("filmGenreDbStorage") FilmGenreStorage filmGenreStorage,
                       @Qualifier("filmUserLikeDbStorage") FilmUserLikeStorage filmUserLikeStorage,
                       @Qualifier("eventDbStorage") EventStorage eventStorage,
                       @Qualifier("genreDbStorage") GenreStorage genreStorage,
                       @Qualifier("ratingMpaDbStorage") RatingMpaStorage ratingMpaStorage,
                       @Qualifier("directorDbStorage") DirectorStorage directorStorage,
                       @Qualifier("filmDirectorDbStorage") FilmDirectorStorage filmDirectorStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmGenreStorage = filmGenreStorage;
        this.filmUserLikeStorage = filmUserLikeStorage;
        this.eventStorage = eventStorage;
        this.cacheRatingMpa = ratingMpaStorage.all().stream()
                .collect(Collectors.toMap(RatingMpa::getRatingMpaId, Function.identity()));
        this.cacheGenre = genreStorage.all().stream()
                .collect(Collectors.toMap(Genre::getGenreId, Function.identity()));
        this.directorStorage = directorStorage;
        this.filmDirectorStorage = filmDirectorStorage;
    }

    private void checkRatingMpaAndGenresFilmDto(FilmDto filmDto) {
        if (filmDto.getMpa() != null && filmDto.getMpa().getId() != null) {
            if (cacheRatingMpa.get(filmDto.getMpa().getId()) == null) {
                throw new BadRequestException("Указанный ID рейтинга MPA не найден - [" + filmDto.getMpa().getId() + "]");
            }
        }
        if (filmDto.getGenres() != null) {
            for (GenreDto genreDto : filmDto.getGenres()) {
                if (cacheGenre.get(genreDto.getId()) == null) {
                    throw new BadRequestException("Указанный ID жанра не найден - [" + genreDto.getId() + "]");
                }
            }
        }
    }

    private void checkDirectorsExist(FilmDto filmDto) {
        Set<Long> directorIdSet = filmDto.getDirectors().stream()
                .map(DirectorDto::getId)
                .collect(Collectors.toSet());

        directorStorage.checkExists(directorIdSet);
    }

    private Set<DirectorDto> getDirectorDtoByFilmId(long id) {
        Set<Director> directors = directorStorage.getDirectorsByFilmId(id);
        if (directors == null) {
            return Collections.emptySet();
        } else {
            return directors.stream()
                    .map(DirectorMapper::mapToDirectorDto)
                    .collect(Collectors.toSet());
        }
    }

    public FilmDto create(FilmDto filmDto) {
        checkRatingMpaAndGenresFilmDto(filmDto);
        if (filmDto.getDirectors() != null && !filmDto.getDirectors().isEmpty()) {
            checkDirectorsExist(filmDto);
        }
        Film film = FilmMapper.mapToFilm(filmDto);
        film = filmStorage.create(film);

        FilmDto filmDtoResponse = FilmMapper.mapToFilmDto(film);
        filmDtoResponse.setGenres(updateFilmGenre(film, filmDto.getGenres()));

        if (film.getRatingMpaId() != null) {
            RatingMpa ratingMpa = cacheRatingMpa.get(film.getRatingMpaId());
            filmDtoResponse.setMpa(RatingMpaMapper.mapToMpaDto(ratingMpa));
        }
        if (filmDto.getDirectors() != null && !filmDto.getDirectors().isEmpty()) {
            filmDirectorStorage.create(film.getId(), filmDto.getDirectors());
            filmDtoResponse.setDirectors(getDirectorDtoByFilmId(film.getId()));
        }

        return filmDtoResponse;
    }

    public FilmDto update(FilmDto filmDto) {
        checkFilmExists(filmDto.getId());
        checkRatingMpaAndGenresFilmDto(filmDto);
        if (filmDto.getDirectors() != null && !filmDto.getDirectors().isEmpty()) {
            checkDirectorsExist(filmDto);
        }
        Film film = FilmMapper.mapToFilm(filmDto);
        film = filmStorage.update(film);

        FilmDto filmDtoResponse = FilmMapper.mapToFilmDto(film);
        filmDtoResponse.setGenres(updateFilmGenre(film, filmDto.getGenres()));

        if (film.getRatingMpaId() != null) {
            RatingMpa ratingMpa = cacheRatingMpa.get(film.getRatingMpaId());
            filmDtoResponse.setMpa(RatingMpaMapper.mapToMpaDto(ratingMpa));
        }
        if (filmDto.getDirectors() != null && !filmDto.getDirectors().isEmpty()) {
            filmDirectorStorage.deleteByFilmId(film.getId());
            filmDirectorStorage.create(film.getId(), filmDto.getDirectors());
            filmDtoResponse.setDirectors(getDirectorDtoByFilmId(film.getId()));
        }

        return filmDtoResponse;
    }

    private List<GenreDto> updateFilmGenre(Film film, List<GenreDto> genreDtos) {
        List<GenreDto> genreDtosUpdated = new ArrayList<>();
        if (genreDtos == null) return genreDtosUpdated;
        filmGenreStorage.removeGenreByFilmId(film.getId());
        Set<Long> genreIds = genreDtos.stream()
                .map(GenreDto::getId)
                .collect(Collectors.toSet());
        for (Long genreId : genreIds) {
            filmGenreStorage.addGenre(film.getId(), genreId);
        }
        genreDtosUpdated = filmGenreStorage.findGenreByFilmId(film.getId()).stream()
                .map(item -> new GenreDto(item.getGenreId(), cacheGenre.get(item.getGenreId()).getName()))
                .collect(Collectors.toList());
        return genreDtosUpdated;
    }

    public List<FilmDto> all() {
        List<Film> films = filmStorage.all();
        return mapFilmsToFilmDtosAndAddDopInfo(films);
    }

    public FilmDto getFilmById(long filmId) {
        checkFilmExists(filmId);
        Film film = filmStorage.findById(filmId);
        return mapFilmsToFilmDtosAndAddDopInfo(Collections.singletonList(film)).get(0);
    }

    private void checkFilmExists(long filmId) {
        if (filmStorage.findById(filmId) == null) {
            throw new NotFoundException("Фильм не найден.");
        }
    }

    private void checkUserExists(long userId) {
        if (userStorage.findById(userId) == null) {
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    public void addLike(long filmId, long userId) {
        checkFilmExists(filmId);
        checkUserExists(userId);
        filmUserLikeStorage.add(filmId, userId);
        eventStorage.create(userId, EventType.LIKE, Operation.ADD, filmId);
    }

    public void deleteLike(long filmId, long userId) {
        checkFilmExists(filmId);
        checkUserExists(userId);
        filmUserLikeStorage.remove(filmId, userId);
        eventStorage.create(userId, EventType.LIKE, Operation.REMOVE, filmId);
    }

    public void clear() {
        filmUserLikeStorage.clear();
        filmStorage.clear();
    }

    public List<FilmDto> findPopularFilms(long count) {
        List<Long> popularFilmsIds = filmUserLikeStorage.popularFilmIds(count);
        List<Film> films = new ArrayList<>();
        for (Long filmId : popularFilmsIds) {
            films.add(filmStorage.findById(filmId));
        }
        return mapFilmsToFilmDtosAndAddDopInfo(films);
    }

    private List<FilmDto> mapFilmsToFilmDtosAndAddDopInfo(List<Film> films) {
        List<FilmDto> filmDtos = new ArrayList<>();
        for (Film film : films) {
            RatingMpa ratingMpa = new RatingMpa();
            if (film.getRatingMpaId() != null) {
                ratingMpa = cacheRatingMpa.get(film.getRatingMpaId());
            }
            List<GenreDto> genreDtos = filmGenreStorage.findGenreByFilmId(film.getId()).stream()
                    .map(item -> new GenreDto(item.getGenreId(), cacheGenre.get(item.getGenreId()).getName()))
                    .collect(Collectors.toList());
            FilmDto filmDto = FilmMapper.mapToFilmDto(film);
            filmDto.setGenres(genreDtos);
            filmDto.setMpa(RatingMpaMapper.mapToMpaDto(ratingMpa));
            filmDto.setDirectors(getDirectorDtoByFilmId(film.getId()));
            filmDtos.add(filmDto);
        }
        return filmDtos;
    }

    public List<FilmDto> getCommonFilms(long userId, long friendId) {
        checkUserExists(userId);
        checkUserExists(friendId);

        List<Long> userLikedFilmIds = filmUserLikeStorage.findFilmLikeByUserId(userId)
                .stream()
                .map(FilmUserLike::getFilmId)
                .collect(Collectors.toList());

        List<Long> friendLikedFilmIds = filmUserLikeStorage.findFilmLikeByUserId(friendId)
                .stream()
                .map(FilmUserLike::getFilmId)
                .collect(Collectors.toList());

        userLikedFilmIds.retainAll(friendLikedFilmIds);

        if (userLikedFilmIds.isEmpty()) {
            return List.of();
        }

        List<Film> films = filmStorage.findByIds(userLikedFilmIds);

        return mapFilmsToFilmDtosAndAddDopInfo(films);
    }

    public List<FilmDto> getFilmsByDirectorIdWithSort(int directorId, String sortBy) {
        Integer count = directorStorage.checkExistsById(directorId);
        if (count == null || count == 0) {
            throw new NotFoundException("Режиссер с id = " + directorId + " не найден");
        }

        List<Film> filmsByDirector = filmStorage.findFilmsByDirector(directorId, sortBy);
        return mapFilmsToFilmDtosAndAddDopInfo(filmsByDirector);
    }
}
