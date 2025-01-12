package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.ReviewLike;
import ru.yandex.practicum.filmorate.storage.ReviewLikeStorage;
import ru.yandex.practicum.filmorate.storage.impl.h2.mappers.ReviewLikeRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Qualifier("reviewLikeDbStorage")
class ReviewLikeDbStorage implements ReviewLikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final ReviewLikeRowMapper reviewLikeRowMapper;

    @Override
    public ReviewLike create(ReviewLike reviewLike) {
        jdbcTemplate.update("INSERT INTO review_like (review_id, user_id, is_like) VALUES (?, ?, ?) ",
                reviewLike.getReviewId(), reviewLike.getUserId(), reviewLike.getIsLike());
        return reviewLike;
    }

    @Override
    public ReviewLike update(ReviewLike reviewLike) {
        String sql = " UPDATE review_like " +
                " SET is_like = ? " +
                " WHERE review_id = ? and user_id=? ";
        jdbcTemplate.update(sql, reviewLike.getIsLike(), reviewLike.getReviewId(), reviewLike.getUserId());
        return reviewLike;
    }

    @Override
    public List<ReviewLike> findByReviewId(long reviewId, long count) {
        return jdbcTemplate.query("SELECT * FROM review_like WHERE review_id = ?",
                reviewLikeRowMapper, reviewId);
    }

    @Override
    public List<ReviewLike> all() {
        return jdbcTemplate.query("SELECT * FROM review_like", reviewLikeRowMapper);
    }

    @Override
    public ReviewLike findByReviewIdAndUserId(long reviewId, long userId) {
        return jdbcTemplate.query("SELECT * FROM review_like WHERE review_id = ? and user_id = ?",
                reviewLikeRowMapper, reviewId, userId).stream().findFirst().orElse(null);
    }

    @Override
    public void deleteByReviewIdAndUserId(long reviewId, long userId) {
        jdbcTemplate.update("DELETE FROM review_like WHERE review_id = ? and user_id = ?", reviewId, userId);
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM review_like");
    }
}
