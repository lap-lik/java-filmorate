package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.SQLDataAccessException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@JdbcTest
@Sql(scripts = "classpath:test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenreDaoDBImplTest {
    private final JdbcTemplate jdbcTemplate;
    private GenreDao genreDao;
    private Genre genre1;
    private Genre genre2;

    private Genre genreUpdate;

    @BeforeEach
    void setUp() {

        genreDao = new GenreDaoDBImpl(jdbcTemplate);
        genre1 = Genre.builder()
                .id(1L)
                .name("Комедия")
                .build();
        genre2 = Genre.builder()
                .id(2L)
                .name("Драма")
                .build();
        genreUpdate = Genre.builder()
                .id(1L)
                .name("Боевик")
                .build();
    }

    @Test
    void testSaveGenreWithExpectedResultNotNull() {

        // вызываем тестируемый метод
        Genre result = genreDao.save(genre1);

        // проверяем утверждения
        assertThat(result)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем значения полей сохраненного жанра
                .isEqualTo(genre1); // проверяем что сохраненный объект и передаваемый идентичны
    }

    @Test
    void testSaveGenreWithEmptyNameResultException() {

        // Подготавливаем данные для теста
        genre1.setName(""); //устанавливаем пустое имя

        // вызываем тестируемый метод и проверяем утверждения
        Throwable exception = assertThrows(SQLDataAccessException.class,
                () -> genreDao.save(genre1)); // проверяем, что DB выбросит исключение SQLDataAccessException
        assertEquals(exception.getMessage(), "Error saving the genre in the DB."); // проверяем текст ошибки
    }

    @Test
    void testUpdateGenreWithExpectedResultNotNull() {

        // Подготавливаем данные для теста
        genreDao.save(genre1);

        // вызываем тестируемый метод
        Genre result = genreDao.update(genreUpdate).orElse(null);

        // проверяем утверждения
        assertThat(result)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем значения полей обновленного жанра
                .isEqualTo(genreUpdate);
    }

    @Test
    void testUpdateGenreWithInvalidGenreIdResultNull() {

        // Подготавливаем данные для теста
        genreDao.save(genre1);
        genreUpdate.setId(999L);

        // вызываем тестируемый метод
        Genre result = genreDao.update(genreUpdate).orElse(null);

        // проверяем утверждения
        assertThat(result)
                .isNull(); // проверяем, что возвращает null так как в DB нет жанра с ID = 999
    }

    @Test
    void testFindGenreByIdWithExpectedResultNotNull() {

        // Подготавливаем данные для теста
        genreDao.save(genre1);
        genreDao.save(genre2);

        // вызываем тестируемый метод
        Genre result = genreDao.findById(2L).orElse(null);

        // проверяем утверждения
        assertThat(result)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем значения полей запрошенного жанра по id=2
                .isEqualTo(genre2);
    }

    @Test
    void testFindGenreByInvalidIdResultNull() {

        // Подготавливаем данные для теста
        genreDao.save(genre1);
        genreDao.save(genre2);

        // вызываем тестируемый метод
        Genre result = genreDao.findById(999L).orElse(null);

        // проверяем утверждения
        assertThat(result)
                .isNull(); // проверяем, что возвращает null так как в DB нет жанра c ID = 999
    }

    @Test
    void testFindAllResultListOfGenres() {

        // Подготавливаем данные для теста
        genreDao.save(genre1);
        genreDao.save(genre2);

        // вызываем тестируемый метод
        List<Genre> result = genreDao.findAll();

        // проверяем утверждения
        assertThat(result.size())
                .isNotNull() // проверяем, что объект не равен null
                .isEqualTo(2); // проверяем что метод возвращает верное количество жанров
    }

    @Test
    void testDeleteByGenreIdResultGenreDeleted() {

        // Подготавливаем данные для теста
        genreDao.save(genre1);
        genreDao.save(genre2);

        // вызываем тестируемый метод
        genreDao.deleteById(1L);

        // проверяем утверждения
        List<Genre> result = genreDao.findAll();
        assertThat(result.size())
                .isNotNull() // проверяем, что объект не равен null
                .isEqualTo(1); // проверяем что удалился жанр с id=1
    }

    @Test
    void testGenreExistenceByIdResultTrue() {

        // Подготавливаем данные для теста
        genreDao.save(genre1);
        genreDao.save(genre2);

        // вызываем тестируемый метод
        boolean result = genreDao.isExistsById(2L);

        // проверяем утверждения
        assertThat(result)
                .isNotNull() // проверяем, что объект не равен null
                .isEqualTo(true); // проверяем что метод подтверждает наличие жанра с id=2 в DB
    }
}