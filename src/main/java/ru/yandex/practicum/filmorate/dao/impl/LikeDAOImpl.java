package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikeDAO;

@Repository
@RequiredArgsConstructor
public class LikeDAOImpl implements LikeDAO {

    public static final String ADD_LIKE = "INSERT INTO likes (film_id, user_id) " +
            "VALUES (?, ?)";
    public static final String DELETE_LIKE = "DELETE " +
            "FROM likes " +
            "WHERE film_id = ? " +
            "  AND user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addLike(Long filmId, Long userId) {

        try {
            jdbcTemplate.update(ADD_LIKE, filmId, userId);
            return true;
        } catch (DuplicateKeyException exception) {
            return false;
        }
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {

        int likeDeleted = jdbcTemplate.update(DELETE_LIKE, filmId, userId);

        return likeDeleted > 0;
    }
}
