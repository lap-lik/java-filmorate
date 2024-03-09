package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDTO;

import java.util.List;

/**
 * The FilmService interface represents a service for managing films.
 * It extends the GenericService interface with FilmDTO as the entity type and Long as the identifier type.
 * The methods provided in this interface allow liking films, retrieving popular films, and deleting films_users.
 *
 * @see GenericService
 */
public interface FilmService extends GenericService<FilmDTO, Long> {

    /**
     * Retrieves a list of popular films.
     *
     * @param count The number of popular films to retrieve as a string.
     * @return A list of FilmDTO objects representing the popular films.
     */
    List<FilmDTO> getPopularFilms(String count);
}
