package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;
import java.util.Set;

public interface RatingMpaStorage {
    List<RatingMpa> all();

    RatingMpa findRatingMpaById(long ratingMpaId);

    List<RatingMpa> findRatingMpaByIds(Set<Long> ratingMpaId);
}
