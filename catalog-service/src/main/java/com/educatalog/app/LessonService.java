package com.educatalog.app;

import com.educatalog.api.dto.LessonRequest;
import com.educatalog.api.dto.LessonResponse;
import com.educatalog.api.mapper.EduMapper;
import com.educatalog.domain.Lesson;
import com.educatalog.domain.Theme;
import com.educatalog.infra.LessonRepository;
import com.educatalog.infra.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonService {

    private final LessonRepository lessonRepository;
    private final ThemeRepository themeRepository;
    private final EduMapper mapper;
    private final org.springframework.kafka.core.KafkaTemplate<String, String> kafkaTemplate;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    public LessonResponse create(LessonRequest request) {
        if (request.themeId() == null) {
            throw new IllegalArgumentException("Theme ID is required");
        }
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException("Theme not found"));

        Lesson lesson = mapper.toLesson(request);
        lesson.setTheme(theme);

        lesson = lessonRepository.save(lesson);
        LessonResponse response = mapper.toLessonResponse(lesson);

        try {
            String message = objectMapper.writeValueAsString(response);
            kafkaTemplate.send("course-created", message);
        } catch (Exception e) {
            // Log error but don't fail transaction? Or fail? Best to fail or use outbox.
            // For TP, we log.
            e.printStackTrace();
        }

        return response;
    }

    @Transactional(readOnly = true)
    public List<LessonResponse> findAll() {
        return lessonRepository.findAll().stream()
                .map(mapper::toLessonResponse)
                .toList();
    }
}
