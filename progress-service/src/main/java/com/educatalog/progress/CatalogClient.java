package com.educatalog.progress;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "catalog-service")
public interface CatalogClient {
    @GetMapping("/api/lessons/{id}")
    LessonDto getLesson(@PathVariable UUID id);

    record LessonDto(UUID id, String title) {
    }
}
