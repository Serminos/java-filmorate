package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review create(Review review);

    Review update(Review review);

    Review findById(long reviewId);

    List<Review> findByFilmId(Long filmId, long limit);

    List<Review> all(long count);

    void clear();

    void deleteReview(long reviewId);
}
