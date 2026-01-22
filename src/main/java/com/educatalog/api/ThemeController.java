package com.educatalog.api;

import com.educatalog.api.dto.LessonRequest;
import com.educatalog.api.dto.LessonResponse;
import com.educatalog.api.dto.ThemeRequest;
import com.educatalog.api.dto.ThemeResponse;
import com.educatalog.app.LessonService;
import com.educatalog.app.ThemeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;
    private final LessonService lessonService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ThemeResponse create(@RequestBody @Valid ThemeRequest request) {
        return themeService.create(request);
    }

    @PostMapping("/{id}/lessons")
    @ResponseStatus(HttpStatus.CREATED)
    public LessonResponse addLesson(@PathVariable UUID id, @RequestBody @Valid LessonRequest request) {
        return lessonService.create(id, request);
    }
}
