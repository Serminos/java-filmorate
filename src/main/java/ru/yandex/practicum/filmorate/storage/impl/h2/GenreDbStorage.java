package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.impl.h2.mappers.GenreRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Qualifier("genreDbStorage")
class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    @Override
    public List<Genre> all() {
        return jdbcTemplate.query(" SELECT * FROM genre ORDER BY genre_id ",
                genreRowMapper);
    }

    @Override
    public Genre findGenreById(long genreId) {
        return jdbcTemplate.query("SELECT * FROM genre WHERE genre_id = ?",
                genreRowMapper, genreId).stream().findFirst().orElse(null);
    }
}
