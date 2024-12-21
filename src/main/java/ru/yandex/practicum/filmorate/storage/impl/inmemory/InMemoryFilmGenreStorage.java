package ru.yandex.practicum.filmorate.storage.impl.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("inMemoryFilmGenreStorage")
public class InMemoryFilmGenreStorage implements FilmGenreStorage {
    private final HashMap<Long, Set<Long>> filmGenres = new HashMap<>();

    @Override
    public void addGenre(long filmId, long genreId) {
        Set<Long> genre = filmGenres.getOrDefault(filmId, Set.of(genreId));
        genre.add(genreId);
        filmGenres.putIfAbsent(filmId, genre);
    }

    @Override
    public void removeGenreByFilmId(long filmId) {
        filmGenres.remove(filmId);
    }

    @Override
    public void removeGenreByFilmIdAndGenreId(long filmId, long genreId) {
        Set<Long> newGenres = filmGenres.get(filmId).stream().filter(c -> c != genreId).collect(Collectors.toSet());
        filmGenres.put(filmId, newGenres);
    }

    @Override
    public List<FilmGenre> all() {
        List<FilmGenre> filmGenresAll = new ArrayList<>();
        for (Map.Entry<Long, Set<Long>> filmGenreEntry : filmGenres.entrySet()) {
            for (Long genreId : filmGenreEntry.getValue()) {
                FilmGenre filmGenre = new FilmGenre();
                filmGenre.setFilmId(filmGenreEntry.getKey());
                filmGenre.setGenreId(genreId);
                filmGenresAll.add(filmGenre);
            }
        }
        return filmGenresAll;
    }

    @Override
    public List<FilmGenre> findGenreByFilmId(long filmId) {
        List<FilmGenre> filmGenreById = new ArrayList<>();
        for (Long genreId : filmGenres.get(filmId)) {
            FilmGenre filmGenre = new FilmGenre();
            filmGenre.setFilmId(filmId);
            filmGenre.setGenreId(genreId);
            filmGenreById.add(filmGenre);
        }
        return filmGenreById;
    }

    @Override
    public List<FilmGenre> findFilmByGenreId(long genreId) {
        List<FilmGenre> filmGenreById = new ArrayList<>();
        for (Map.Entry<Long, Set<Long>> filmGenreEntry : filmGenres.entrySet()) {
            for (Long genreIdIter : filmGenreEntry.getValue()) {
                if (genreIdIter == genreId) {
                    FilmGenre filmGenre = new FilmGenre();
                    filmGenre.setFilmId(filmGenreEntry.getKey());
                    filmGenre.setGenreId(genreIdIter);
                    filmGenreById.add(filmGenre);
                }
            }
        }
        return filmGenreById;
    }

    private long getNextId() {
        long currentMaxId = filmGenres.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
