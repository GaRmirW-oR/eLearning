package com.educatalog.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LessonRequest(
        @NotBlank(message = "Title is required") String title,
        String content) {
}
