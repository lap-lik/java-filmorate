package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

/**
 * The FilmDao interface represents a data access object for managing films.
 * It extends the GenericDao interface with Film as the entity type and Long as the identifier type.
 * The method provided in this interface allow  retrieving popular films.
 *
 * @see GenericDao
 */
public interface FilmDao extends GenericDao<Film, Long> {

    /**
     * Retrieves a list of popular films.
     *
     * @param count The number of popular films to retrieve.
     * @return A list of popular Film objects.
     */
    List<Film> findPopularFilms(int count);
}
