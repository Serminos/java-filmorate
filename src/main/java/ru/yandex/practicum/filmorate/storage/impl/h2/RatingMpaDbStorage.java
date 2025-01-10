package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;
import ru.yandex.practicum.filmorate.storage.impl.h2.mappers.RatingMpaRowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Qualifier("ratingMpaDbStorage")
class RatingMpaDbStorage implements RatingMpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RatingMpaRowMapper ratingMpaRowMapper;

    @Override
    public List<RatingMpa> all() {
        return jdbcTemplate.query(" SELECT * FROM rating_mpa ", ratingMpaRowMapper);
    }

    @Override
    public RatingMpa findRatingMpaById(long ratingMpaId) {
        return jdbcTemplate.query("SELECT * FROM rating_mpa WHERE rating_mpa_id = ?",
                ratingMpaRowMapper, ratingMpaId).stream().findFirst().orElse(null);
    }

    @Override
    public List<RatingMpa> findRatingMpaByIds(Set<Long> ratingMpaIds) {
        String inSql = String.join(",", Collections.nCopies(ratingMpaIds.size(), "?"));

        List<RatingMpa> ratingMpaList = jdbcTemplate.query(
                String.format("SELECT * FROM EMPLOYEE WHERE id IN (%s)", inSql), ratingMpaRowMapper,
                ratingMpaIds.toArray());
        return ratingMpaList;
    }
}
