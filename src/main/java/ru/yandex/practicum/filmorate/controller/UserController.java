package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FriendService friendService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody final UserDTO userDTO) {

        log.info("START endpoint `method:POST /users` (create user), request: {}.", userDTO.getLogin());

        return userService.create(userDTO);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable final Long id) {

        log.info("START endpoint `method:GET /users/{id}` (get user by id), user id: {}.", id);

        return userService.getById(id);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {

        log.info("START endpoint `method:GET /users` (get all users).");

        return userService.getAll();
    }

    @PutMapping
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {

        log.info("START endpoint `method:PUT /users` (update user), request: {}.", userDTO.getLogin());

        return userService.update(userDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {

        log.info("START endpoint `method:DELETE /users/{id}` (delete user by id), user id: {}.", id);
        userService.deleteById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {

        log.info("START endpoint `method:PUT /users/{id}/friends/{friendId}` (add friend), " +
                "user id: {}, friend id: {}.", id, friendId);
        friendService.addFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<UserDTO> getAllFriends(@PathVariable Long id) {

        log.info("START endpoint `method:GET /users/{id}/friends` (get all friends), user id: {}.", id);

        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserDTO> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {

        log.info("START endpoint `method:GET /users/{id}/friends/common/{otherId}` (get common friends), " +
                "user id: {}, other user id: {}.", id, otherId);

        return userService.getCommonFriends(id, otherId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriendById(@PathVariable Long id, @PathVariable Long friendId) {

        log.info("START endpoint `method:DELETE /users/{id}/friends/{friendId}` (delete friend), " +
                "user id: {}, friend id: {}.", id, friendId);
        friendService.deleteFriendById(id, friendId);
    }
}
