package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.utils.ValidatorUtils;
import ru.yandex.practicum.filmorate.validation.Marker;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmDao filmDao;
    private final FilmMapper filmMapper;

    @Override
    public FilmDTO create(FilmDTO filmDTO) {

        ValidatorUtils.validate(filmDTO, Marker.OnCreate.class);

        return filmMapper.toDTO(filmDao.save(filmMapper.toEntity(filmDTO)));
    }

    @Override
    public FilmDTO getById(Long filmId) {

        return filmMapper.toDTO(filmDao.findById(filmId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The film with the ID - `%d` was not found.", filmId))
                        .httpStatus(NOT_FOUND)
                        .build()));
    }

    @Override
    public List<FilmDTO> getAll() {

        return filmMapper.toDTOs(filmDao.findAll());
    }

    @Override
    public FilmDTO update(FilmDTO filmDTO) {

        ValidatorUtils.validate(filmDTO, Marker.OnUpdate.class);

        return filmMapper.toDTO(filmDao.update(filmMapper.toEntity(filmDTO))
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The film `%s` was not found.", filmDTO.getName()))
                        .httpStatus(NOT_FOUND)
                        .build()));
    }

    @Override
    public void deleteById(Long filmId) {

        boolean isFilmDeleted = filmDao.deleteById(filmId);

        if (!isFilmDeleted) {
            throw NotFoundException.builder()
                    .message(String.format("The film with the ID - `%d` was not found.", filmId))
                    .httpStatus(NOT_FOUND)
                    .build();
        }
    }

    @Override
    public List<FilmDTO> getPopularFilms(String count) {

        return filmMapper.toDTOs(filmDao.findPopularFilms(Integer.parseInt(count)));
    }
}
