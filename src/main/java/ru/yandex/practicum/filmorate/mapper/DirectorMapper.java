package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.DirectorDTO;
import ru.yandex.practicum.filmorate.model.Director;

@Mapper(componentModel = "spring")
public interface DirectorMapper extends GenericMapper<Director, DirectorDTO> {
}