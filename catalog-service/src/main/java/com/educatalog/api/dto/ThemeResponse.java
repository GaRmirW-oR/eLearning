package com.educatalog.api.dto;

import java.util.UUID;

public record ThemeResponse(
        UUID id,
        String name,
        String description) {
}
