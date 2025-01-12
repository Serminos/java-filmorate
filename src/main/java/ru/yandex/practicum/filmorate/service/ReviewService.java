package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.ReviewLike;
import ru.yandex.practicum.filmorate.service.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.storage.ReviewLikeStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    private final UserStorage userStorage;
    private final FilmService filmService;
    private final ReviewStorage reviewStorage;
    private final ReviewLikeStorage reviewLikeStorage;

    @Autowired
    public ReviewService(@Qualifier("userDbStorage") UserStorage userStorage,
                         FilmService filmService, @Qualifier("reviewDbStorage") ReviewStorage reviewStorage, ReviewLikeStorage reviewLikeStorage) {
        this.userStorage = userStorage;
        this.filmService = filmService;
        this.reviewStorage = reviewStorage;
        this.reviewLikeStorage = reviewLikeStorage;
    }

    public ReviewDto create(ReviewDto reviewDto) {
        checkReview(reviewDto);
        ReviewDto reviewDtoCreated = ReviewMapper.mapToReviewDto(reviewStorage.create(ReviewMapper.mapToReview(reviewDto)));
        reviewDtoCreated.setUseful(0);
        return reviewDtoCreated;
    }

    private void checkReviewExists(long reviewId) {
        if (reviewStorage.findById(reviewId) == null) {
            throw new NotFoundException("Не найден отзыв с ID - [" + reviewId + "]");
        }
    }

    private void checkReview(ReviewDto reviewDto) {
        if (userStorage.findById(reviewDto.getUserId()) == null) {
            throw new NotFoundException("Не найден пользователь с ID - [" + reviewDto.getUserId() + "]");
        }
        if (filmService.getFilmById(reviewDto.getFilmId()) == null) {
            throw new NotFoundException("Не найден фильм с ID - [" + reviewDto.getUserId() + "]");
        }
    }

    public ReviewDto update(ReviewDto reviewDto) {
        checkReviewExists(reviewDto.getReviewId());
        Review review = ReviewMapper.mapToReview(reviewDto);
        reviewStorage.update(review);
        ReviewDto reviewDto1 = ReviewMapper.mapToReviewDto(review);
        reviewDto1.setUseful(getUseful(review));
        return reviewDto1;
    }

    public void deleteById(long reviewId) {
        reviewStorage.deleteReview(reviewId);
    }

    public void addLike(long reviewId, long userId) {
        ReviewLike reviewLike = new ReviewLike(reviewId, userId, true);
        if (reviewLikeStorage.findByReviewIdAndUserId(reviewId, userId) != null) {
            reviewLikeStorage.deleteByReviewIdAndUserId(reviewId, userId);
        }
        reviewLikeStorage.create(reviewLike);
    }

    public void addDislike(long reviewId, long userId) {
        ReviewLike reviewDislike = new ReviewLike(reviewId, userId, false);
        if (reviewLikeStorage.findByReviewIdAndUserId(reviewId, userId) != null) {
            reviewLikeStorage.deleteByReviewIdAndUserId(reviewId, userId);
        }
        reviewLikeStorage.create(reviewDislike);
    }

    public void removeLike(long reviewId, long userId) {
        reviewLikeStorage.deleteByReviewIdAndUserId(reviewId, userId);
    }

    public void removeDislike(long reviewId, long userId) {
        reviewLikeStorage.deleteByReviewIdAndUserId(reviewId, userId);
    }

    public List<ReviewDto> getAllReview(long count) {
        List<ReviewDto> reviewDtos = new ArrayList<>();
        List<Review> reviews = reviewStorage.all(count);
        if (reviews.isEmpty()) {
            return reviewDtos;
        }
        for (Review review : reviews) {
            reviewDtos.add(ReviewMapper.mapToReviewDto(review));
        }
        return reviewDtos;
    }

    public ReviewDto getReviewById(long reviewId) {
        Review review = reviewStorage.findById(reviewId);
        if (review == null) {
            throw new NotFoundException("Отзыв не найден.");
        }
        ReviewDto reviewDto = ReviewMapper.mapToReviewDto(review);
        reviewDto.setUseful(getUseful(review));
        return reviewDto;
    }

    private Integer getUseful(Review review) {
        List<ReviewLike> reviewLikes = reviewLikeStorage.findByReviewId(review.getId(), 0);
        return reviewLikes.isEmpty() ? 0 : reviewLikes.stream().mapToInt(item -> item.getIsLike() ? 1 : -1).sum();
    }

    public List<ReviewDto> getReviewByFilmId(Long filmId, Long count) {
        List<ReviewDto> reviewDtos = new ArrayList<>();
        List<Review> reviews = new ArrayList<>();

        if (filmId == null) {
            reviews = reviewStorage.all(count);
        } else {
            reviews = reviewStorage.findByFilmId(filmId, count);
        }

        if (reviews.isEmpty()) {
            return new ArrayList<>();
        }

        for (Review review : reviews) {
            ReviewDto reviewDto1 = ReviewMapper.mapToReviewDto(review);
            reviewDto1.setUseful(getUseful(review));
            reviewDtos.add(reviewDto1);
        }

        return reviewDtos;
    }

    public List<ReviewDto> all(long limit) {
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review : reviewStorage.all(limit)) {
            reviewDtos.add(ReviewMapper.mapToReviewDto(review));
        }
        return reviewDtos;
    }

    public void clear() {
        reviewLikeStorage.clear();
        reviewStorage.clear();
    }
}
