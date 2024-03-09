package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.ReviewDTO;
import ru.yandex.practicum.filmorate.model.Review;
@Mapper(componentModel = "spring")
public interface ReviewMapper extends GenericMapper <Review, ReviewDTO>{
}
