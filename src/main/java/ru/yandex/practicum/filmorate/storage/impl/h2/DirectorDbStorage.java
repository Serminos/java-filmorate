package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.impl.h2.mappers.DirectorRowMapper;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
@AllArgsConstructor
@Qualifier("directorDbStorage")
public class DirectorDbStorage implements DirectorStorage {

    private JdbcTemplate jdbcTemplate;
    private DirectorRowMapper directorRowMapper;

    @Override
    public List<Director> getAllDirectors() {
        log.info("Получен запрос на получение списка всех режиссеров");
        final String GET_ALL_DIRECTORS = "SELECT * from directors;";
        return jdbcTemplate.query(GET_ALL_DIRECTORS, directorRowMapper);
    }

    @Override
    public Director getDirectorById(int id) {
        log.info("Получен запрос на поиск режиссера по его id: {}", id);
        final String GET_DIRECTOR_BY_ID = "SELECT director_id, name FROM directors WHERE director_id = ?;";
        try {
            return jdbcTemplate.queryForObject(GET_DIRECTOR_BY_ID, directorRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Режиссер с id = " + id + " не найден");
        }
    }

    @Override
    public Director createDirector(Director director) {
        log.info("Получен запрос на добавление фильма: {}", director);

        final String CREATE_DIRECTOR = "INSERT INTO directors (name) VALUES (?);";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_DIRECTOR, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);

        final Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && keys.containsKey("director_id")) {
            final Integer generatedId = (Integer) keys.get("director_id");
            director.setId(generatedId);
        } else {
            throw new RuntimeException("Не удалось получить сгенерированный ID для режиссера");
        }
        log.info("Добавление режиссера: {} - закончено, присвоен id: {}", director, director.getId());
        return director;
    }

    @Override
    public Director updateDirector(Director newDirector) {
        log.info("Получен запрос на обновление режиссера: {}", newDirector);

        final String UPDATE_DIRECTOR = "UPDATE directors SET name = ? WHERE director_id = ?;";
        int newDirectorId = newDirector.getId();
        jdbcTemplate.update(UPDATE_DIRECTOR, newDirector.getName(), newDirectorId);

        final Director updatedDirector = getDirectorById(newDirectorId);
        log.info("Обновление режиссера: {} - закончено.", updatedDirector);
        return updatedDirector;
    }

    @Override
    public void deleteDirectorById(int id) {
        log.info("Получен запрос на удаление режиссера с id: {}", id);

        final String DELETE_DIRECTOR_BY_ID = "DELETE FROM directors WHERE director_id = ?;";

        int rowsAffected = jdbcTemplate.update(DELETE_DIRECTOR_BY_ID, id);
        if (rowsAffected == 0) {
            log.warn("Попытка удаления: режиссер с id = {} не найден", id);
        } else {
            log.info("Режиссер с id = {} успешно удален", id);
        }
    }

    @Override
    public Set<Director> getDirectorsByFilmId(long id) {
        log.info("Получен запрос на получение режиссера/-ов фильма с id: {}", id);

        final String GET_DIRECTORS_BY_FILM_ID = """
                SELECT d.director_id, d.name
                FROM directors d
                JOIN film_director fd ON d.director_id = fd.director_id
                WHERE fd.film_id = ?
                """;

        Set<Director> directors = new HashSet<>(jdbcTemplate.query(GET_DIRECTORS_BY_FILM_ID, directorRowMapper, id));
        log.info("Список режиссеров для фильма с id {} подготовлен. Найдено {} режиссеров.", id, directors.size());
        return directors;
    }


    @Override
    public void checkDirectorsExist(Set<Integer> directorIdSet) {
        final String CHECK_DIRECTOR_EXISTS = """
                SELECT COUNT(*)
                FROM directors
                WHERE director_id = ?;
                """;

        for (Integer directorId : directorIdSet) {
            Integer count = jdbcTemplate.queryForObject(CHECK_DIRECTOR_EXISTS, Integer.class, directorId);
            if (count == null || count == 0) {
                throw new NotFoundException("Режиссер с id = " + directorId + " не найден");
            }
        }
    }

    @Override
    public void checkDirectorExistById(int directorId) {
        final String CHECK_DIRECTOR_EXISTS = """
                SELECT COUNT(*)
                FROM directors
                WHERE director_id = ?;
                """;

        Integer count = jdbcTemplate.queryForObject(CHECK_DIRECTOR_EXISTS, Integer.class, directorId);
        if (count == null || count == 0) {
            throw new NotFoundException("Режиссер с id = " + directorId + " не найден");
        }
    }
}
