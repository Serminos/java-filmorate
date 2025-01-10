package ru.yandex.practicum.filmorate.storage.impl.h2.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmUserLike;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmUserLikeRowMapper implements RowMapper<FilmUserLike> {

    @Override
    public FilmUserLike mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmUserLike filmUserLike = new FilmUserLike();
        filmUserLike.setFilmId(rs.getLong("film_id"));
        filmUserLike.setUserId(rs.getLong("user_id"));
        return filmUserLike;
    }
}
