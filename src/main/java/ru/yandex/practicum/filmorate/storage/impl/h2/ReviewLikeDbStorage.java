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

    private static final String CREATE = """
            MERGE INTO review_like (review_id, user_id, is_like)
            KEY (review_id, user_id)
            VALUES (?, ?, ?);
            """;
    private static final String UPDATE = """
            UPDATE review_like
            SET is_like = ?
            WHERE review_id = ? and user_id=?;
            """;
    private static final String FIND_BY_REVIEW_ID = " SELECT * FROM review_like WHERE review_id = ?; ";
    private static final String GET_ALL = " SELECT * FROM review_like; ";
    private static final String FIND_BY_REVIEW_ID_AND_USER_ID = """
            SELECT * FROM review_like
            WHERE review_id = ? and user_id = ?;
            """;
    private static final String DELETE_BY_REVIEW_ID_AND_USER_ID = """
            DELETE FROM review_like
            WHERE review_id = ? and user_id = ?;
            """;

    @Override
    public ReviewLike create(ReviewLike reviewLike) {
        jdbcTemplate.update(CREATE, reviewLike.getReviewId(), reviewLike.getUserId(), reviewLike.getIsLike());
        return reviewLike;
    }

    @Override
    public ReviewLike update(ReviewLike reviewLike) {
        jdbcTemplate.update(UPDATE, reviewLike.getIsLike(), reviewLike.getReviewId(), reviewLike.getUserId());
        return reviewLike;
    }

    @Override
    public List<ReviewLike> findByReviewId(long reviewId) {
        return jdbcTemplate.query(FIND_BY_REVIEW_ID, reviewLikeRowMapper, reviewId);
    }

    @Override
    public List<ReviewLike> getAll() {
        return jdbcTemplate.query(GET_ALL, reviewLikeRowMapper);
    }

    @Override
    public ReviewLike findByReviewIdAndUserId(long reviewId, long userId) {
        return jdbcTemplate.query(FIND_BY_REVIEW_ID_AND_USER_ID,
                reviewLikeRowMapper, reviewId, userId).stream().findFirst().orElse(null);
    }

    @Override
    public void deleteByReviewIdAndUserId(long reviewId, long userId) {
        jdbcTemplate.update(DELETE_BY_REVIEW_ID_AND_USER_ID, reviewId, userId);
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM review_like");
    }
}
