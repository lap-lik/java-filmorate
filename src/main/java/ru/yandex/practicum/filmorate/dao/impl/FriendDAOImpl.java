package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendDAO;

@Repository
@RequiredArgsConstructor
public class FriendDAOImpl implements FriendDAO {

    public static final String ADD_FRIEND = "INSERT INTO friendships (user_1, user_2) " +
            "SELECT ?, ? " +
            "FROM DUAL " +
            "WHERE NOT EXISTS (SELECT 1 " +
            "                  FROM friendships" +
            "                  WHERE (user_1 = ? AND user_2 = ?) " +
            "                     OR (user_1 = ? AND user_2 = ?))";
    public static final String DELETE_FRIEND = "DELETE " +
            "FROM friendships " +
            "WHERE (user_1 = ? AND user_2 = ?) " +
            "   OR (user_1 = ? AND user_2 = ?)";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addFriend(Long userId, Long friendId) {

        int addedFriend = jdbcTemplate.update(ADD_FRIEND, userId, friendId, userId, friendId, friendId, userId);

        return addedFriend > 0;
    }

    @Override
    public boolean deleteFriend(Long userId, Long friendId) {

        int friendDeleted = jdbcTemplate.update(DELETE_FRIEND, userId, friendId, friendId, userId);

        return friendDeleted > 0;
    }
}
