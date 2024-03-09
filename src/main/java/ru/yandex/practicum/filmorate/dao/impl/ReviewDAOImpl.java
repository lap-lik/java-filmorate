package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.ReviewDAO;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewDAOImpl implements ReviewDAO {

    public static final String SAVE_REVIEW = "INSERT INTO reviews (content, is_positive, film_id, user_id, useful) "
            + "SELECT ?, ?, ?, ?, ? FROM dual WHERE EXISTS (SELECT 1 FROM users WHERE id = ?) "
            + "AND EXISTS (SELECT 1 FROM films WHERE id = ?) " +
            "AND NOT EXISTS(SELECT 1 FROM reviews WHERE film_id = ? and user_id = ?)";
    public static final String FIND_REVIEWS = "SELECT * " +
            "FROM genres";
    public static final String FIND_REVIEWS_BY_ID = FIND_REVIEWS + " WHERE id = ?";
    public static final String UPDATE_REVIEW = "UPDATE genres " +
            "SET name = ? " +
            "WHERE id = ?";
    public static final String DELETE_REVIEW_BY_ID = "DELETE " +
            "FROM genres " +
            "WHERE id = ?";
    public static final String IS_EXIST_REVIEW_BY_ID = "SELECT EXISTS (SELECT 1 FROM genres WHERE id = ?)";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Review> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Review> findAll() {
        return null;
    }

    @Override
    public Review save(Review review) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(SAVE_REVIEW, new String[]{"id"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setInt(3, review.getFilmId().intValue());
            stmt.setInt(4, review.getUserId().intValue());
            stmt.setInt(5, review.getUseful().intValue());
            stmt.setInt(6, review.getUserId().intValue());
            stmt.setInt(7, review.getFilmId().intValue());
            stmt.setInt(8, review.getFilmId().intValue());
            stmt.setInt(9, review.getUserId().intValue());
            return stmt;
        }, keyHolder);

        Long reveiwId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        review.setReviewId(reveiwId);

        return review;
    }

    @Override
    public Optional<Review> update(Review entity) {
        return Optional.empty();
    }

    @Override
    public boolean deleteById(Long aLong) {
        return false;
    }

    @Override
    public boolean isExistsById(Long aLong) {
        return false;
    }
}
