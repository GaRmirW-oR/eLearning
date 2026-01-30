package com.educatalog.api;

import com.educatalog.api.dto.LessonResponse;
import com.educatalog.app.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public List<LessonResponse> getAll() {
        return lessonService.findAll();
    }

    @org.springframework.web.bind.annotation.PostMapping
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public LessonResponse create(
            @org.springframework.web.bind.annotation.RequestBody @jakarta.validation.Valid com.educatalog.api.dto.LessonRequest request) {
        return lessonService.create(request);
    }
}
