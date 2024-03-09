package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorDAO;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DirectorDAOImpl implements DirectorDAO {

    public static final String SAVE_DIRECTOR = "INSERT INTO directors (name) " +
            "VALUES (?)";
    public static final String FIND_DIRECTORS = "SELECT *" +
            "FROM directors";
    public static final String FIND_DIRECTOR_BY_ID = FIND_DIRECTORS +
            " WHERE id = ?";
    public static final String UPDATE_DIRECTOR = "UPDATE directors " +
            "SET name = ? " +
            "WHERE id = ?";
    public static final String DELETE_DIRECTOR_BY_ID = "DELETE " +
            "FROM directors " +
            "WHERE id = ?";
    public static final String IS_EXIST_DIRECTOR_BY_ID = "SELECT EXISTS(SELECT 1 FROM directors WHERE id = ?)";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Director> findById(Long id) {

        List<Director> directors = jdbcTemplate.query(FIND_DIRECTOR_BY_ID, this::mapRowToDirector, id);

        return directors.stream().findFirst();
    }

    @Override
    public List<Director> findAll() {

        return jdbcTemplate.query(FIND_DIRECTORS, this::mapRowToDirector);
    }

    @Override
    public Director save(Director director) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(SAVE_DIRECTOR, new String[]{"id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);

        Long directorId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        director.setId(directorId);

        return findById(directorId).orElse(null);
    }

    @Override
    public Optional<Director> update(Director director) {

        int update = jdbcTemplate.update(UPDATE_DIRECTOR, director.getName(), director.getId());
        if (update == 0) {
            return Optional.empty();
        }

        return findById(director.getId());
    }

    @Override
    public boolean deleteById(Long id) {

        return jdbcTemplate.update(DELETE_DIRECTOR_BY_ID, id) > 0;
    }

    @Override
    public boolean isExistsById(Long id) {

        return jdbcTemplate.queryForObject(IS_EXIST_DIRECTOR_BY_ID, Boolean.class, id);
    }

    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
