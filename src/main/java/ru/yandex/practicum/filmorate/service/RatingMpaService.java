package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.mapper.RatingMpaMapper;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.util.List;

@Service
public class RatingMpaService {

    private final RatingMpaStorage ratingMpaStorage;

    public RatingMpaService(@Qualifier("ratingMpaDbStorage") RatingMpaStorage ratingMpaStorage) {
        this.ratingMpaStorage = ratingMpaStorage;
    }

    public List<MpaDto> findAllRatingMpa() {
        return ratingMpaStorage.all().stream().map(RatingMpaMapper::mapToMpaDto).toList();
    }

    public MpaDto findRatingMpaById(int id) {
        RatingMpa ratingMpa = ratingMpaStorage.findRatingMpaById(id);
        if (ratingMpa == null) {
            throw new NotFoundException("Рейтинг MPA не найден.");
        }
        return RatingMpaMapper.mapToMpaDto(ratingMpa);
    }
}
