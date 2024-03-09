package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.ReviewDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDTO createUser(@RequestBody final ReviewDTO reviewDTO) {

        log.info("START endpoint `method:POST /reviews` (create review), request: {}.", reviewDTO.getContent());


        return reviewDTO;
    }
}
