package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.DirectorDTO;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.validation.Marker;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService service;

    @GetMapping("/{id}")
    public DirectorDTO getDirectorById(@PathVariable Long id) {

        log.info("Запрос GET, на получение режиссёра, по id: {}.", id);

        return service.getById(id);
    }

    @GetMapping
    public List<DirectorDTO> getAllDirectors() {

        log.info("Запрос GET, на получения всех режиссёров.");

        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DirectorDTO createDirector(@RequestBody DirectorDTO directorDTO) {

        log.info("Запрос Post, на оздание режиссёра: {}.", directorDTO.getName());

        return service.create(directorDTO);
    }

    @PutMapping
    @Validated({Marker.OnUpdate.class})
    public DirectorDTO updateDirector(@RequestBody DirectorDTO directorDTO) {

        log.info("Запрос Put, на обновления данных режиссёра с id: {}", directorDTO.getId());

        return service.update(directorDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        log.info("Запрос DELETE, на удаления режиссёра, по id: {}", id);
        service.deleteById(id);
    }
}
