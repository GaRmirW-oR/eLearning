package com.educatalog.api.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record ThemeRequest(
        @NotBlank(message = "Name is required") String name,
        String description) {
}
