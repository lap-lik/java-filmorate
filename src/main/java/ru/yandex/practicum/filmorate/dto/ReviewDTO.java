package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@Data
@Builder
public class ReviewDTO {
    @Null(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnUpdate.class, message = "The ID must not be empty.")
    private Long reviewId;

    @NotBlank(message = "The content of the review must not be empty.")
    private String content;

    private Boolean isPositive;

    @NotNull(message = "UserId must not not be null")
    @Positive(message = "The userId of the review should be positive.")
    private Long userId;

    @NotNull(message = "FilmId must not not be null")
    @Positive(message = "The filmId of the review should be positive.")
    private Long filmId;

    @NotBlank(message = "The useful of the review must not be empty.")
    private Long useful;
}
