package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DirectorService {

    private final DirectorStorage directorStorage;

    public List<DirectorDto> getAll() {
        List<Director> directors = directorStorage.getAllDirectors();

        List<DirectorDto> allDirectorsDto = new ArrayList<>();
        for (Director director : directors) {
            DirectorDto directorDto = DirectorMapper.mapToDirectorDto(director);
            allDirectorsDto.add(directorDto);
        }
        return allDirectorsDto;
    }

    public DirectorDto getById(long id) {
        Director director;
        try {
            director = directorStorage.findDirectorById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Режиссер с id = " + id + " не найден");
        }
        return DirectorMapper.mapToDirectorDto(director);
    }

    public DirectorDto create(DirectorDto directorDto) {
        return DirectorMapper.mapToDirectorDto(directorStorage.createDirector(DirectorMapper.mapToDirector(directorDto)));
    }

    public DirectorDto update(DirectorDto newDirectorDto) {
        Long directorId = newDirectorDto.getId();
        if (directorId == null) {
            throw new BadRequestException("Id должен быть указан");
        }

        Integer count = directorStorage.checkExistsById(directorId);
        if (count == null || count == 0) {
            throw new NotFoundException("Режиссер с id = " + directorId + " не найден");
        }


        return DirectorMapper.mapToDirectorDto(directorStorage.updateDirector(DirectorMapper.mapToDirector(newDirectorDto)));
    }

    public void deleteById(long id) {
        if (directorStorage.deleteDirectorById(id) == 0) {
            log.warn("Попытка удаления: режиссер с id = [{}] не найден", id);
        } else {
            log.trace("Режиссер с id = [{}] успешно удален", id);
        }
    }
}
