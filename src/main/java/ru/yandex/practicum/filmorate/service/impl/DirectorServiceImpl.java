package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDAO;
import ru.yandex.practicum.filmorate.dto.DirectorDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.utils.ValidatorUtils;
import ru.yandex.practicum.filmorate.validation.Marker;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {
    private final DirectorDAO directorDAO;
    private final DirectorMapper directorMapper;

    @Override
    public DirectorDTO getById(Long id) {

        return directorMapper.toDTO(directorDAO.findById(id)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The director with the ID - `%d` was not found.", id))
                        .httpStatus(NOT_FOUND)
                        .build()));
    }

    @Override
    public List<DirectorDTO> getAll() {

        return directorMapper.toDTOs(directorDAO.findAll());
    }

    @Override
    public DirectorDTO create(DirectorDTO directorDTO) {

        ValidatorUtils.validate(directorDTO);

        return directorMapper.toDTO(directorDAO.save(directorMapper.toEntity(directorDTO)));
    }

    @Override
    public DirectorDTO update(DirectorDTO directorDTO) {

        ValidatorUtils.validate(directorDTO, Marker.OnUpdate.class);

        return directorMapper.toDTO(directorDAO.update(directorMapper.toEntity(directorDTO))
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The director with the ID - `%d` was not found.", directorDTO.getId()))
                        .httpStatus(NOT_FOUND)
                        .build()));
    }

    @Override
    public void deleteById(Long id) {

        boolean deleted = directorDAO.deleteById(id);

        if (!deleted) {
            throw NotFoundException.builder()
                    .message(String.format("The director with the ID - `%d` was not found.", id))
                    .httpStatus(NOT_FOUND)
                    .build();
        }
    }
}

