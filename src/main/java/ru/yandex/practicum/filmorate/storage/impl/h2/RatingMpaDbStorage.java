package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;
import ru.yandex.practicum.filmorate.storage.impl.h2.mappers.RatingMpaRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Qualifier("ratingMpaDbStorage")
class RatingMpaDbStorage implements RatingMpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RatingMpaRowMapper ratingMpaRowMapper;

    private static final String GET_ALL = " SELECT * FROM rating_mpa; ";
    private static final String FIND_BY_ID = " SELECT * FROM rating_mpa WHERE rating_mpa_id = ?; ";

    @Override
    public List<RatingMpa> getAll() {
        return jdbcTemplate.query(GET_ALL, ratingMpaRowMapper);
    }

    @Override
    public RatingMpa findByRatingMpaId(long ratingMpaId) {
        return jdbcTemplate.query(FIND_BY_ID,
                ratingMpaRowMapper, ratingMpaId).stream().findFirst().orElse(null);
    }
}
