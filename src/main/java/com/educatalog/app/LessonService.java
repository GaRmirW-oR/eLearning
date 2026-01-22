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

    public LessonResponse create(UUID themeId, LessonRequest request) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("Theme not found"));

        Lesson lesson = mapper.toLesson(request);
        lesson.setTheme(theme);

        lesson = lessonRepository.save(lesson);
        return mapper.toLessonResponse(lesson);
    }

    @Transactional(readOnly = true)
    public List<LessonResponse> findAll() {
        // Optimized findAll uses @EntityGraph in Repository
        return lessonRepository.findAll().stream()
                .map(mapper::toLessonResponse)
                .toList();
    }
}
