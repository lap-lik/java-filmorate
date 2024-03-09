package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDAO;
import ru.yandex.practicum.filmorate.exception.SQLDataAccessException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class FilmDAOImpl implements FilmDAO {
    public static final String SAVE_FILM = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    public static final String FIND_FILMS = "SELECT f.*," +
            "       m.name AS mpa_name," +
            "       g.id   AS genre_id," +
            "       g.name AS genre_name," +
            "       d.id   AS director_id," +
            "       d.name AS director_name " +
            "FROM films AS f" +
            "         LEFT OUTER JOIN mpa AS m ON f.mpa_id = m.id" +
            "         LEFT OUTER JOIN films_genres AS fg ON f.id = fg.film_id" +
            "         LEFT OUTER JOIN genres AS g ON fg.genre_id = g.id" +
            "         LEFT OUTER JOIN films_directors AS fd ON f.id = fd.film_id" +
            "         LEFT OUTER JOIN directors AS d ON fd.director_id = d.id ";
    public static final String FIND_POPULAR_FILMS = FIND_FILMS + " LEFT OUTER JOIN films_users AS l ON l.film_id = f.id " +
            "          GROUP BY f.id" +
            "          ORDER BY COUNT(l.user_id) DESC" +
            "          LIMIT ?";
    public static final String FIND_FILM_BY_ID = FIND_FILMS +
            " WHERE f.id = ?";
    public static final String UPDATE_FILM = "UPDATE films " +
            "SET name         = ?," +
            "    description  = ?," +
            "    release_date = ?," +
            "    duration     = ?," +
            "    mpa_id       = ? " +
            "WHERE id = ?";
    public static final String DELETE_FILM_BY_ID = "DELETE " +
            "FROM films " +
            "WHERE id = ?";
    public static final String IS_EXIST_FILM_BY_ID = "SELECT EXISTS (SELECT 1 FROM films WHERE id = ?)";
    public static final String ADD_LINKS_FILMS_GENRES = "INSERT INTO films_genres (film_id, genre_id) " +
            "VALUES (?, ?)";
    public static final String DELETE_LINKS_FILMS_GENRES = "DELETE " +
            "FROM films_genres " +
            "WHERE film_id = ?";
    public static final String ADD_LINKS_FILMS_DIRECTORS = "INSERT INTO films_directors(film_id, director_id) " +
            "VALUES (?, ?)";
    public static final String DELETE_LINKS_FILMS_DIRECTORS = "DELETE FROM films_directors " +
            "WHERE film_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public Film save(Film film) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(SAVE_FILM, new String[]{"id"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                stmt.setInt(4, film.getDuration());
                stmt.setInt(5, film.getMpa().getId().intValue());
                return stmt;
            }, keyHolder);

            Long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();

            film.setId(filmId);
            updateFilmGenresLinks(film);
            updateFilmDirectorsLinks(film);

            return findById(filmId).orElse(null);
        } catch (DataAccessException exception) {
            throw new SQLDataAccessException("Error saving the film in the DB.", exception);
        }
    }

    @Override
    public Optional<Film> findById(Long filmId) {

        List<Film> films = jdbcTemplate.query(FIND_FILM_BY_ID, this::extractToFilms, filmId);

        return films.stream().findFirst();
    }

    @Override
    public List<Film> findAll() {

        return jdbcTemplate.query(FIND_FILMS, this::extractToFilms);
    }

    @Override
    public Optional<Film> update(Film film) {

        Long filmId = film.getId();
        try {
            int rowsUpdated = jdbcTemplate.update(UPDATE_FILM, film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), filmId);

            if (rowsUpdated == 0) {
                return Optional.empty();
            }

            updateFilmGenresLinks(film);
            updateFilmDirectorsLinks(film);

            return findById(filmId);
        } catch (DataAccessException exception) {
            throw new SQLDataAccessException("Error updating the film in the DB.", exception);
        }
    }

    @Override
    public boolean deleteById(Long filmId) {

        int filmDeleted = jdbcTemplate.update(DELETE_FILM_BY_ID, filmId);
        return filmDeleted > 0;
    }

    @Override
    public boolean isExistsById(Long filmId) {

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(IS_EXIST_FILM_BY_ID, Boolean.class, filmId));
    }

    @Override
    public List<Film> findPopularFilms(int count) {

        return jdbcTemplate.query(FIND_POPULAR_FILMS, this::extractToFilms, count);
    }

    private void updateFilmGenresLinks(Film film) {

        Long filmId = film.getId();
        deleteLinksFilmGenre(filmId);

        if (film.getGenres() == null) {
            return;
        }

        List<Genre> genres = new ArrayList<>(film.getGenres());

        jdbcTemplate.batchUpdate(ADD_LINKS_FILMS_GENRES, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {

                preparedStatement.setLong(1, film.getId());
                preparedStatement.setLong(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    private void deleteLinksFilmGenre(Long filmId) {

        jdbcTemplate.update(DELETE_LINKS_FILMS_GENRES, filmId);
    }

    private void updateFilmDirectorsLinks(Film film) {

        Long filmId = film.getId();
        deleteFilmDirectorsLinks(filmId);

        if (film.getDirectors() == null) {
            return;
        }

        List<Director> directors = new ArrayList<>(film.getDirectors());

        jdbcTemplate.batchUpdate(ADD_LINKS_FILMS_DIRECTORS, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {

                preparedStatement.setLong(1, film.getId());
                preparedStatement.setLong(2, directors.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return directors.size();
            }
        });
    }

    private void deleteFilmDirectorsLinks(Long filmId) {

        jdbcTemplate.update(DELETE_LINKS_FILMS_DIRECTORS, filmId);
    }

    private List<Film> extractToFilms(ResultSet rs) throws SQLException {
        Map<Long, Film> films = new HashMap<>();

        while (rs.next()) {
            Long filmId = rs.getLong("id");
            Film film = films.get(filmId);

            if (film == null) {
                film = Film.builder()
                        .id(filmId)
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .releaseDate(rs.getDate("release_date").toLocalDate())
                        .duration(rs.getInt("duration"))
                        .genres(new TreeSet<>(Comparator.comparing(Genre::getId)))
                        .directors(new TreeSet<>(Comparator.comparing(Director::getId)))
                        .build();

                films.put(filmId, film);
            }
            Long mpaId = rs.getLong("mpa_id");
            String mpaName = rs.getString("mpa_name");

            Mpa mpa = Mpa.builder()
                    .id(mpaId)
                    .name(mpaName)
                    .build();
            film.setMpa(mpa);

            Long genreId = rs.getLong("genre_id");
            String genreName = rs.getString("genre_name");

            if (genreName != null) {
                Genre genre = Genre.builder()
                        .id(genreId)
                        .name(genreName)
                        .build();
                film.getGenres().add(genre);
            }

            Long directorId = rs.getLong("director_id");
            String directorName = rs.getString("director_name");

            if (directorName != null) {
                Director director = Director.builder()
                        .id(directorId)
                        .name(directorName)
                        .build();
                film.getDirectors().add(director);
            }
        }

        return new ArrayList<>(films.values());
    }
}
