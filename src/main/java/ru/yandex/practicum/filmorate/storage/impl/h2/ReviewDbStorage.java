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

    @Override
    public Review create(Review review) {
        String sql = "INSERT INTO review (content, is_positive, user_id, film_id, useful) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"review_id"});
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
        String sql = "UPDATE review SET content = ?, is_positive = ?, " +
                "user_id = ?, film_id = ?, useful = ? WHERE review_id = ?";
        int res = jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getUserId(),
                review.getFilmId(), review.getUseful(), review.getId());
        return review;
    }

    @Override
    public Review findById(long reviewId) {
        return jdbcTemplate.query("SELECT * FROM review WHERE review_id = ? ORDER BY useful",
                reviewRowMapper, reviewId).stream().findFirst().orElse(null);
    }

    @Override
    public List<Review> findByFilmId(Long filmId, long limit) {
        return jdbcTemplate.query("SELECT * FROM review WHERE film_id = ? ORDER BY useful LIMIT ?",
                reviewRowMapper, filmId, limit);
    }

    @Override
    public List<Review> all(long limit) {
        return jdbcTemplate.query("SELECT * FROM review ORDER BY useful LIMIT ?", reviewRowMapper, limit);
    }

    @Override
    public void deleteReview(long reviewId) {
        jdbcTemplate.update("DELETE FROM review WHERE review_id = ?", reviewId);
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM review");
    }
}
