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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DirectorService {

    private final DirectorStorage directorStorage;

    public List<DirectorDto> getAll() {
        List<Director> directors = directorStorage.getAll();
        return directors.stream().map(DirectorMapper::mapToDirectorDto).collect(Collectors.toList());
    }

    public DirectorDto getById(long id) {
        Director director;
        try {
            director = directorStorage.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Режиссер с id = " + id + " не найден");
        }
        return DirectorMapper.mapToDirectorDto(director);
    }

    public DirectorDto create(DirectorDto directorDto) {
        return DirectorMapper.mapToDirectorDto(directorStorage.create(DirectorMapper.mapToDirector(directorDto)));
    }

    public DirectorDto update(DirectorDto newDirectorDto) {
        Long directorId = newDirectorDto.getId();
        if (directorId == null) {
            throw new BadRequestException("Id должен быть указан");
        }

        Integer count = directorStorage.existsByDirectorId(directorId);
        if (count == null || count == 0) {
            throw new NotFoundException("Режиссер с id = " + directorId + " не найден");
        }


        return DirectorMapper.mapToDirectorDto(directorStorage.update(DirectorMapper.mapToDirector(newDirectorDto)));
    }

    public void deleteById(long directorId) {
        if (directorStorage.deleteByDirectorId(directorId) == 0) {
            log.warn("Попытка удаления: режиссер с director_id = [{}] не найден", directorId);
        } else {
            log.trace("Режиссер с id = [{}] успешно удален", directorId);
        }
    }
}
