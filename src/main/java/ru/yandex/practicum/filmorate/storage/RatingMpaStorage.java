package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;

public interface RatingMpaStorage {
    List<RatingMpa> getAll();

    RatingMpa findByRatingMpaId(long ratingMpaId);
}
