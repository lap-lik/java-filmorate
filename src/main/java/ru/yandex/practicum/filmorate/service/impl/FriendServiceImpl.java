package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDAO;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendDAO friendDAO;
    private final UserDao userDao;

    @Override
    public void addFriend(Long userId, Long friendId) {

        checkIds(userId, friendId);
        boolean isFriendAdded = friendDAO.addFriend(userId, friendId);

        if (!isFriendAdded) {
            throw BadRequestException.builder()
                    .message(String.format("The friend by ID - `%d` has already been added.", friendId))
                    .httpStatus(BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public void deleteFriendById(Long userId, Long friendId) {

        checkIds(userId, friendId);

        boolean isFriendDeleted = friendDAO.deleteFriend(userId, friendId);

        if (!isFriendDeleted) {
            throw BadRequestException.builder()
                    .message(String.format("The user by ID - `%d` was not found in the friends list.", friendId))
                    .httpStatus(BAD_REQUEST)
                    .build();
        }
    }

    private void checkIds(Long firstUserId, Long secondUserId) {

        List<String> messages = new ArrayList<>();
        boolean firstUserExists = userDao.isExistsById(firstUserId);
        boolean secondUserExists = userDao.isExistsById(secondUserId);

        if (!firstUserExists) {
            messages.add(String.format("The user by ID - `%d` was not found.", firstUserId));
        }

        if (!secondUserExists) {
            messages.add(String.format("The user by ID - `%d` was not found.", secondUserId));
        }

        if (!messages.isEmpty()) {
            throw NotFoundException.builder()
                    .message(String.join(" & ", messages))
                    .httpStatus(NOT_FOUND)
                    .build();
        }
    }
}
