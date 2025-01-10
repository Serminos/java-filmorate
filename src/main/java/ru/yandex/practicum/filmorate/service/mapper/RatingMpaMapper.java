package ru.yandex.practicum.filmorate.service.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.RatingMpa;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RatingMpaMapper {

    public static MpaDto mapToMpaDto(RatingMpa ratingMpa) {
        MpaDto mpaDto = new MpaDto();
        mpaDto.setId(ratingMpa.getRatingMpaId());
        mpaDto.setName(ratingMpa.getName());
        return mpaDto;
    }

    public static RatingMpa mapToRatingMpa(MpaDto ratingMpaDto) {
        RatingMpa ratingMpa = new RatingMpa();
        ratingMpa.setRatingMpaId(ratingMpaDto.getId());
        ratingMpa.setName(ratingMpaDto.getName());
        return ratingMpa;
    }
}
