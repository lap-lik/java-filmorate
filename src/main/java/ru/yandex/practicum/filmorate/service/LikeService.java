package ru.yandex.practicum.filmorate.service;
/**
 * The LikeService interface represents a service for managing like links.
 * The methods provided in this interface allow you to like and delete movies.
 */
public interface LikeService {

    /**
     * Likes a film by the given film ID and user ID.
     *
     * @param filmId The ID of the film to be liked.
     * @param userId The ID of the user who films_users the film.
     */
    void likeFilm(Long filmId, Long userId);

    /**
     * Deletes a previously liked film by the given film ID and user ID.
     *
     * @param filmId The ID of the film to be unliked.
     * @param userId The ID of the user who unfilms_users the film.
     */
    void deleteLike(Long filmId, Long userId);
}
