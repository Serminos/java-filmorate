package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DirectorService {

    private final DirectorStorage directorStorage;

    public List<DirectorDto> getAllDirectors() {
        List<Director> directors = directorStorage.getAllDirectors();

        List<DirectorDto> allDirectorsDto = new ArrayList<>();
        for (Director director : directors) {
            DirectorDto directorDto = DirectorMapper.mapToDirectorDto(director);
            allDirectorsDto.add(directorDto);
        }
        return allDirectorsDto;
    }

    public DirectorDto getDirectorById(int id) {
        Director director = directorStorage.getDirectorById(id);
        return DirectorMapper.mapToDirectorDto(director);
    }

    public DirectorDto createDirector(DirectorDto directorDto) {
        return DirectorMapper.mapToDirectorDto(directorStorage.createDirector(DirectorMapper.mapToDirector(directorDto)));
    }

    public DirectorDto updateDirector(DirectorDto newDirectorDto) {
        Integer directorId = newDirectorDto.getId();
        if (directorId == null) {
            throw new BadRequestException("Id должен быть указан");
        }
        directorStorage.checkDirectorExistById(directorId);

        return DirectorMapper.mapToDirectorDto(directorStorage.updateDirector(DirectorMapper.mapToDirector(newDirectorDto)));
    }

    public void deleteDirectorById(int id) {
        directorStorage.deleteDirectorById(id);
    }
}
