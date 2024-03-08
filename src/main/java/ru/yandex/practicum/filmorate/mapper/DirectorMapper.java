package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.DirectorDTO;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DirectorMapper {
    Director toModel(DirectorDTO directorDTO);

    DirectorDTO toDTO(Director director);

    List<Director> toListModels(List<DirectorDTO> directorDTOList);

    List<DirectorDTO> toListDTO(List<Director> directorList);
}