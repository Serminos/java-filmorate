package ru.yandex.practicum.filmorate.storage.impl.h2.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingMpaRowMapper implements RowMapper<RatingMpa> {

    @Override
    public RatingMpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        RatingMpa ratingMpa = new RatingMpa();
        ratingMpa.setRatingMpaId(rs.getLong("rating_mpa_id"));
        ratingMpa.setName(rs.getString("name"));
        String description = rs.getString("description");
        if (!description.isEmpty()) {
            ratingMpa.setDescription(rs.getString("description"));
        }
        return ratingMpa;
    }
}
