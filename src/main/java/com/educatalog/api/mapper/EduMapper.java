package com.educatalog.api.mapper;

import com.educatalog.api.dto.*;
import com.educatalog.domain.Lesson;
import com.educatalog.domain.Theme;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EduMapper {

    Theme toTheme(ThemeRequest request);

    ThemeResponse toThemeResponse(Theme theme);

    // Lesson mapping
    @Mapping(target = "theme", ignore = true) // Theme is set manually in service
    Lesson toLesson(LessonRequest request);

    LessonResponse toLessonResponse(Lesson lesson);
}
