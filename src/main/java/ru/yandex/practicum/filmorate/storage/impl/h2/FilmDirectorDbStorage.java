package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.storage.FilmDirectorStorage;

import java.util.ArrayList;
import java.util.List;

import java.util.Set;

@Slf4j
@Repository
@AllArgsConstructor
@Qualifier("filmDirectorDbStorage")
public class FilmDirectorDbStorage implements FilmDirectorStorage {

    private JdbcTemplate jdbcTemplate;

    @Override
    public void createConnectionFilmDirectors(Long filmId, Set<DirectorDto> directors) {

        final String INSERT_FILM_DIRECTOR = """
                INSERT INTO film_director (film_id, director_id)
                VALUES (?, ?);
                """;

        List<Object[]> params = new ArrayList<>();

        for (DirectorDto director : directors) {
            Object[] row = new Object[]{filmId, director.getId()};
            params.add(row);
        }

        int[] updateCounts = jdbcTemplate.batchUpdate(INSERT_FILM_DIRECTOR, params);
        int addedCount = 0;
        for (int count : updateCounts) {
            addedCount += count;
        }
        log.info("Добавлено {} записей в таблицу film_director для фильма с id = {}", addedCount, filmId);
    }

    @Override
    public void deleteConnectionFilmDirectorsByFilmId(Long filmId) {

        final String DELETE_FILM_DIRECTOR = """
                DELETE FROM film_director
                WHERE film_id = ?;""";
        int deletedCount = jdbcTemplate.update(DELETE_FILM_DIRECTOR, filmId);
        if (deletedCount > 0) {
            log.info("Удалено {} записей из таблицы film_director для фильма с id = {}", deletedCount, filmId);
        } else {
            log.warn("Не найдено записей для удаления в таблице film_director для фильма с id = {}", filmId);
        }
    }
}
