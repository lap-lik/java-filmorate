//package ru.yandex.practicum.filmorate.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/reviews")
//public class ReviewController {
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public ReveiwDTO createUser(@RequestBody final ReveiwDTO reveiwDTO) {
//
//        log.info("START endpoint `method:POST /reviews` (create review), request: {}.", reveiwDTO.getContent());
//
//
//
//        String sql =  "INSERT INTO reviews (content, is_positive, film_id, user_id) "
//                + "SELECT ?, ?, ?, ? FROM dual WHERE EXISTS (SELECT 1 FROM users WHERE id = ?) "
//                + "AND EXISTS (SELECT 1 FROM films WHERE id = ?) " +
//                "AND NOT EXISTS(SELECT 1 FROM reviews WHERE film_id = ? and user_id = ?)";
//
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(connection -> {
//            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
//            stmt.setString(1, reveiwDTO.getContent());
//            stmt.setBoolean(2, reveiwDTO.getIsPositive());
//            stmt.setInt(3, reveiwDTO.getFilmId().intValue());
//            stmt.setInt(4, reveiwDTO.getUserId().intValue());
//            stmt.setInt(5, reveiwDTO.getUserId().intValue());
//            stmt.setInt(6, reveiwDTO.getFilmId().intValue());
//            stmt.setInt(7, reveiwDTO.getFilmId().intValue());
//            stmt.setInt(8, reveiwDTO.getUserId().intValue());
//            return stmt;
//        }, keyHolder);
//
//        Long reveiwId = Objects.requireNonNull(keyHolder.getKey()).longValue();
//
//        reveiwDTO.setId(reveiwId);
//
//        return reveiwDTO;
//    }
//}
