package ru.yandex.practicum.filmorate.dao;

/**
 * The LikeDAO interface represents a data access object for managing like links.
 * The methods provided in this interface allow adding films_users to film and removing films_users.
 */
public interface LikeDAO {

    /**
     * Likes a film with the specified film ID and user ID.
     *
     * @param filmId The ID of the film to be liked.
     * @param userId The ID of the user who films_users the film.
     * @return true if the like is added successfully, false otherwise.
     */
    boolean addLike(final Long filmId, final Long userId);

    /**
     * Removes a like from a film with the specified film ID and user ID.
     *
     * @param filmId The ID of the film to be unliked.
     * @param userId The ID of the user who unfilms_users the film.
     * @return true if the like is removed successfully, false otherwise.
     */
    boolean deleteLike(final Long filmId, final Long userId);
}