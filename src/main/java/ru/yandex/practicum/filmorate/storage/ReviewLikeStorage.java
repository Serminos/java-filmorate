package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.ReviewLike;

import java.util.List;

public interface ReviewLikeStorage {
    ReviewLike create(ReviewLike review);

    ReviewLike update(ReviewLike review);

    List<ReviewLike> all();

    List<ReviewLike> findByReviewId(long reviewId, long count);

    ReviewLike findByReviewIdAndUserId(long reviewId, long userId);

    void deleteByReviewIdAndUserId(long reviewId, long userId);

    void clear();
}
