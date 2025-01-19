package ru.yandex.practicum.filmorate.storage.impl.h2;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private static final String GET_ALL_DIRECTORS = "SELECT * from directors;";
    private static final String GET_DIRECTOR_BY_ID = "SELECT director_id, name FROM directors WHERE director_id = ?;";
    private static final String CREATE_DIRECTOR = "INSERT INTO directors (name) VALUES (?);";
    private static final String UPDATE_DIRECTOR = "UPDATE directors SET name = ? WHERE director_id = ?;";
    private static final String DELETE_DIRECTOR_BY_ID = "DELETE FROM directors WHERE director_id = ?;";
    private static final String GET_DIRECTORS_BY_FILM_ID = """
                SELECT d.director_id, d.name
                FROM directors d
                JOIN film_director fd ON d.director_id = fd.director_id
                WHERE fd.film_id = ?
                """;
    private static final String CHECK_DIRECTOR_EXISTS = """
                SELECT COUNT(*)
                FROM directors
                WHERE director_id = ?;
                """;
    private static final String FIND_BY_NAME = " SELECT * " +
            " FROM DIRECTORS " +
            " WHERE lower(NAME) like '%'||lower(?)||'%' ";


    @Override
    public List<Director> getAll() {
        return jdbcTemplate.query(GET_ALL_DIRECTORS, directorRowMapper);
    }

    @Override
    public Director findById(long directorId) {
        return jdbcTemplate.queryForObject(GET_DIRECTOR_BY_ID, directorRowMapper, directorId);

    }

    @Override
    public Director create(Director director) {
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_DIRECTOR, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);

        final Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && keys.containsKey("director_id")) {
            final Long generatedId = (Long) keys.get("director_id");
            director.setId(generatedId);
        } else {
            throw new RuntimeException("Не удалось получить сгенерированный ID для режиссера");
        }
        log.trace("Добавление режиссера: [{}] - закончено, присвоен id: [{}]", director, director.getId());
        return director;
    }

    @Override
    public Director update(Director newDirector) {
        long newDirectorId = newDirector.getId();
        jdbcTemplate.update(UPDATE_DIRECTOR, newDirector.getName(), newDirectorId);

        final Director updatedDirector = findById(newDirectorId);
        log.trace("Обновление режиссера: [{}] - закончено.", updatedDirector);
        return updatedDirector;
    }

    @Override
    public Integer deleteById(long directorId) {
        return jdbcTemplate.update(DELETE_DIRECTOR_BY_ID, directorId);
    }

    @Override
    public Set<Director> findDirectorsByFilmId(long filmId) {
        log.trace("Получение режиссера/-ов фильма с id: [{}]", filmId);
        Set<Director> directors = new HashSet<>(jdbcTemplate.query(GET_DIRECTORS_BY_FILM_ID, directorRowMapper, filmId));
        log.trace("Список режиссеров для фильма с id [{}] подготовлен. Найдено [{}] режиссеров.", filmId, directors.size());
        return directors;
    }


    @Override
    public void checkExists(Set<Long> directorIdSet) {
        for (Long directorId : directorIdSet) {
            Integer count = jdbcTemplate.queryForObject(CHECK_DIRECTOR_EXISTS, Integer.class, directorId);
            if (count == null || count == 0) {
                throw new NotFoundException("Режиссер с id = " + directorId + " не найден");
            }
        }
    }

    @Override
    public Integer checkExistsById(long directorId) {
        return jdbcTemplate.queryForObject(CHECK_DIRECTOR_EXISTS, Integer.class, directorId);
    }

    @Override
    public List<Director> findByNameContainingIgnoreCase(String query) {
        return jdbcTemplate.query(FIND_BY_NAME, directorRowMapper, query);
    }
}