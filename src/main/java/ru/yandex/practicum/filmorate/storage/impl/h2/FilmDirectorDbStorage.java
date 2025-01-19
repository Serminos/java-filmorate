package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.FilmDirector;
import ru.yandex.practicum.filmorate.storage.FilmDirectorStorage;
import ru.yandex.practicum.filmorate.storage.impl.h2.mappers.FilmDirectorRowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@AllArgsConstructor
@Qualifier("filmDirectorDbStorage")
public class FilmDirectorDbStorage implements FilmDirectorStorage {

    private JdbcTemplate jdbcTemplate;
    private FilmDirectorRowMapper filmDirectorRowMapper;

    private static final String CREATE = """
                INSERT INTO film_director (film_id, director_id)
                VALUES (?, ?);
                """;
    private static final String DELETE_BY_FILM_ID = """
                DELETE FROM film_director
                WHERE film_id = ?;""";
    private static final String FIND_BY_DIRECTOR_ID_IN = " SELECT * FROM film_director WHERE director_id IN (?) ";


    @Override
    public void create(Long filmId, Set<DirectorDto> directors) {
        List<Object[]> params = new ArrayList<>();
        for (DirectorDto director : directors) {
            Object[] row = new Object[]{filmId, director.getId()};
            params.add(row);
        }

        int[] updateCounts = jdbcTemplate.batchUpdate(CREATE, params);
        int addedCount = 0;
        for (int count : updateCounts) {
            addedCount += count;
        }
        log.trace("Добавлено [{}] записей в таблицу film_director для фильма с id = [{}]", addedCount, filmId);
    }

    @Override
    public void deleteByFilmId(Long filmId) {


        int deletedCount = jdbcTemplate.update(DELETE_BY_FILM_ID, filmId);
        if (deletedCount > 0) {
            log.trace("Удалено [{}] записей из таблицы film_director для фильма с id = [{}]", deletedCount, filmId);
        } else {
            log.trace("Не найдено записей для удаления в таблице film_director для фильма с id = [{}]", filmId);
        }
    }

    @Override
    public List<FilmDirector> findByDirectorIdIn(List<Long> directorIds) {
        if (directorIds.isEmpty()) {
            return List.of();
        }
        return jdbcTemplate.query(FIND_BY_DIRECTOR_ID_IN, filmDirectorRowMapper, directorIds.toArray());
    }
}
