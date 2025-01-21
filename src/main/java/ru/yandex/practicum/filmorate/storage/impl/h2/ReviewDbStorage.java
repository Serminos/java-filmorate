package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.impl.h2.mappers.ReviewRowMapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Qualifier("reviewDbStorage")
class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private final ReviewRowMapper reviewRowMapper;

    private static final String CREATE = """
            INSERT INTO review
            (content, is_positive, user_id, film_id, useful)
            VALUES (?, ?, ?, ?, ?);
            """;
    private static final String UPDATE = """
            UPDATE review
            SET content = ?, is_positive = ?, useful = ?
            WHERE review_id = ?;
            """;
    private static final String FIND_BY_ID = " SELECT * FROM review WHERE review_id = ? ORDER BY useful DESC; ";
    private static final String FIND_BY_FILM_ID = """
            SELECT * FROM review
            WHERE film_id = ?
            ORDER BY useful DESC LIMIT ?;
            """;
    private static final String GET_ALL = " SELECT * FROM review ORDER BY useful DESC LIMIT ?; ";
    private static final String DELETE_BY_ID = " DELETE FROM review WHERE review_id = ?; ";

    @Override
    public Review create(Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(CREATE, new String[]{"review_id"});
                    ps.setString(1, review.getContent());
                    ps.setBoolean(2, review.getIsPositive());
                    ps.setLong(3, review.getUserId());
                    ps.setLong(4, review.getFilmId());
                    ps.setInt(5, review.getUseful());
                    return ps;
                }, keyHolder);
        review.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return review;
    }

    @Override
    public Review update(Review review) {
        int res = jdbcTemplate.update(UPDATE, review.getContent(), review.getIsPositive(),
                review.getUseful(), review.getId());
        return review;
    }

    @Override
    public Review findByReviewId(long reviewId) {
        return jdbcTemplate.query(FIND_BY_ID,
                reviewRowMapper, reviewId).stream().findFirst().orElse(null);
    }

    @Override
    public List<Review> findByFilmId(Long filmId, long limit) {
        return jdbcTemplate.query(FIND_BY_FILM_ID, reviewRowMapper, filmId, limit);
    }

    @Override
    public List<Review> getAll(long limit) {
        return jdbcTemplate.query(GET_ALL, reviewRowMapper, limit);
    }

    @Override
    public void deleteById(long reviewId) {
        jdbcTemplate.update(DELETE_BY_ID, reviewId);
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM review");
    }
}
