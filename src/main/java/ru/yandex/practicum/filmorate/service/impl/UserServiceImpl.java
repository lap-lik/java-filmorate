package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDAO;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.utils.ValidatorUtils;
import ru.yandex.practicum.filmorate.validation.Marker;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDao;
    private final UserMapper userMapper;

    @Override
    public UserDTO create(UserDTO userDTO) {

        ValidatorUtils.validate(userDTO, Marker.OnCreate.class);
        ValidatorUtils.validateUserName(userDTO);

        return userMapper.toDTO(userDao.save(userMapper.toEntity(userDTO)));
    }

    @Override
    public UserDTO getById(Long userId) {

        return userMapper.toDTO(userDao.findById(userId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The user with the ID - `%d` was not found.", userId))
                        .httpStatus(NOT_FOUND)
                        .build()));
    }

    @Override
    public List<UserDTO> getAll() {

        return userMapper.toDTOs(userDao.findAll());
    }

    @Override
    public UserDTO update(UserDTO userDTO) {

        ValidatorUtils.validate(userDTO, Marker.OnUpdate.class);
        ValidatorUtils.validateUserName(userDTO);

        return userMapper.toDTO(userDao.update(userMapper.toEntity(userDTO))
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The user `%s` was not found.", userDTO.getLogin()))
                        .httpStatus(NOT_FOUND)
                        .build()));
    }

    @Override
    public void deleteById(Long userId) {

        boolean isUserDeleted = userDao.deleteById(userId);

        if (!isUserDeleted) {
            throw NotFoundException.builder()
                    .message(String.format("The user with the ID - `%d` was not found.", userId))
                    .httpStatus(NOT_FOUND)
                    .build();
        }
    }

    @Override
    public List<UserDTO> getAllFriends(Long userId) {

        if (!userDao.isExistsById(userId)) {
            throw NotFoundException.builder()
                    .message(String.format("The user with the ID - `%d` was not found.", userId))
                    .httpStatus(NOT_FOUND)
                    .build();
        }

        return userMapper.toDTOs(userDao.findAllFriends(userId));
    }

    @Override
    public List<UserDTO> getCommonFriends(Long userId, Long otherId) {

        checkIds(userId, otherId);

        return userMapper.toDTOs(userDao.findCommonFriends(userId, otherId));
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