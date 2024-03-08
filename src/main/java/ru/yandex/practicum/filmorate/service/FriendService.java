package ru.yandex.practicum.filmorate.service;
/**
 * The FriendService interface represents a service for managing friend links.
 * The methods provided in this interface allow adding and deleting friends for a user.
 */
public interface FriendService {

    /**
     * Adds a friend to the user with the specified ID.
     *
     * @param id       The ID of the user.
     * @param friendId The ID of the friend to be added.
     */
    void addFriend(Long id, Long friendId);

    /**
     * Deletes a friend from the user with the specified ID.
     *
     * @param id       The ID of the user.
     * @param friendId The ID of the friend to be deleted.
     */
    void deleteFriendById(Long id, Long friendId);
}
