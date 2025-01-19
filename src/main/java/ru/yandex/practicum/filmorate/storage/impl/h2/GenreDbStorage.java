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

    private static final String GET_ALL = " SELECT * FROM genre ORDER BY genre_id; ";
    private static final String FIND_BY_ID = "SELECT * FROM genre WHERE genre_id = ?; ";

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(GET_ALL, genreRowMapper);
    }

    @Override
    public Genre findByGenreId(long genreId) {
        return jdbcTemplate.query(FIND_BY_ID, genreRowMapper, genreId).stream().findFirst().orElse(null);
    }
}
