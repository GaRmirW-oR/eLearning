package com.educatalog.api.dto;

import java.util.UUID;

public record LessonResponse(
        UUID id,
        String title,
        String content,
        ThemeResponse theme) {
}
