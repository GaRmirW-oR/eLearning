package com.educatalog.api.mapper;

import com.educatalog.api.dto.LessonRequest;
import com.educatalog.api.dto.LessonResponse;
import com.educatalog.api.dto.ThemeRequest;
import com.educatalog.api.dto.ThemeResponse;
import com.educatalog.domain.Lesson;
import com.educatalog.domain.Theme;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-22T12:27:44+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Ubuntu)"
)
@Component
public class EduMapperImpl implements EduMapper {

    @Override
    public Theme toTheme(ThemeRequest request) {
        if ( request == null ) {
            return null;
        }

        Theme theme = new Theme();

        theme.setName( request.name() );
        theme.setDescription( request.description() );

        return theme;
    }

    @Override
    public ThemeResponse toThemeResponse(Theme theme) {
        if ( theme == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String description = null;

        id = theme.getId();
        name = theme.getName();
        description = theme.getDescription();

        ThemeResponse themeResponse = new ThemeResponse( id, name, description );

        return themeResponse;
    }

    @Override
    public Lesson toLesson(LessonRequest request) {
        if ( request == null ) {
            return null;
        }

        Lesson lesson = new Lesson();

        lesson.setTitle( request.title() );
        lesson.setContent( request.content() );

        return lesson;
    }

    @Override
    public LessonResponse toLessonResponse(Lesson lesson) {
        if ( lesson == null ) {
            return null;
        }

        UUID id = null;
        String title = null;
        String content = null;
        ThemeResponse theme = null;

        id = lesson.getId();
        title = lesson.getTitle();
        content = lesson.getContent();
        theme = toThemeResponse( lesson.getTheme() );

        LessonResponse lessonResponse = new LessonResponse( id, title, content, theme );

        return lessonResponse;
    }
}
