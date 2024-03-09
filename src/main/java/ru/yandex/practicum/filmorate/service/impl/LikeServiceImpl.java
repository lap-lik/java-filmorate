package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDAO;
import ru.yandex.practicum.filmorate.dao.LikeDAO;
import ru.yandex.practicum.filmorate.dao.UserDAO;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.LikeService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeDAO likeDAO;
    private final FilmDAO filmDao;
    private final UserDAO userDao;

    @Override
    public void likeFilm(Long filmId, Long userId) {

        checkIds(filmId, userId);
        boolean isAddedLike = likeDAO.addLike(filmId, userId);

        if (!isAddedLike) {
            throw BadRequestException.builder()
                    .message(String.format("The user with the ID - `%d` has already liked the film with the ID - `%d`.",
                            userId, filmId))
                    .httpStatus(BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {

        checkIds(filmId, userId);
        boolean isDeletedLike = likeDAO.deleteLike(filmId, userId);

        if (!isDeletedLike) {
            throw BadRequestException.builder()
                    .message(String.format("The user with the ID - `%d` did not like the film with the ID - `%d`.",
                            userId, filmId))
                    .httpStatus(BAD_REQUEST)
                    .build();
        }
    }

    private void checkIds(Long filmId, Long userId) {

        List<String> messages = new ArrayList<>();
        boolean filmExists = filmDao.isExistsById(filmId);
        boolean userExists = userDao.isExistsById(userId);

        if (!filmExists) {
            messages.add(String.format("The film by ID - `%d` was not found.", filmId));
        }

        if (!userExists) {
            messages.add(String.format("The user by ID - `%d` was not found.", userId));
        }

        if (!messages.isEmpty()) {
            throw NotFoundException.builder()
                    .message(String.join(" & ", messages))
                    .httpStatus(NOT_FOUND)
                    .build();
        }
    }
}
