package com.educatalog.api;

import com.educatalog.api.dto.LessonResponse;
import com.educatalog.app.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public List<LessonResponse> getAll() {
        return lessonService.findAll();
    }
}
