package ru.yandex.practicum.filmorate.storage.impl.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("inMemoryGenreStorage")
public class InMemoryGenreStorage implements GenreStorage {
    private final HashMap<Long, String> genres = new HashMap<>();

    public InMemoryGenreStorage() {
        genres.put(1L, "Комедия");
        genres.put(2L, "Драма");
        genres.put(3L, "Мультфильм");
        genres.put(4L, "Триллер");
        genres.put(5L, "Документальный");
        genres.put(6L, "Боевик");
    }

    @Override
    public List<Genre> all() {
        List<Genre> genreAll = new ArrayList<>();
        for (Map.Entry<Long, String> genreEntry : genres.entrySet()) {
            Genre genre = new Genre();
            genre.setGenreId(genreEntry.getKey());
            genre.setName(genreEntry.getValue());
            genreAll.add(genre);
        }
        return genreAll;
    }

    @Override
    public Genre findGenreById(long genreId) {
        Genre findGenreById = new Genre();
        if (genres.get(genreId) != null) {
            findGenreById.setGenreId(genreId);
            findGenreById.setName(genres.get(genreId));
        }
        return findGenreById;
    }
}
