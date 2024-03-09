package ru.yandex.practicum.filmorate.dao;

/**
 * The FriendDAO interface represents a data access object for managing friend l    inks.
 * The methods provided in this interface allow adding, retrieving, and deleting friends for a user.
 */
public interface FriendDAO {

    /**
     * Adds a friend to the user with the specified user ID.
     *
     * @param userId   The ID of the user.
     * @param friendId The ID of the friend to be added.
     * @return true if the friend is added successfully, false otherwise.
     */
    boolean addFriend(final Long userId, final Long friendId);

    /**
     * Deletes a friend from the user with the specified user ID.
     *
     * @param userId   The ID of the user.
     * @param friendId The ID of the friend to be deleted.
     * @return true if the friend is deleted successfully, false otherwise.
     */
    boolean deleteFriend(final Long userId, final Long friendId);
}
