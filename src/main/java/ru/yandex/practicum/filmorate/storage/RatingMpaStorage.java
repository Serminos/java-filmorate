package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;
import java.util.Set;

public interface RatingMpaStorage {
    List<RatingMpa> getAll();

    RatingMpa findById(long ratingMpaId);

    List<RatingMpa> findByIds(Set<Long> ratingMpaId);
}
