package com.educatalog.app;

import com.educatalog.api.dto.ThemeRequest;
import com.educatalog.api.dto.ThemeResponse;
import com.educatalog.api.mapper.EduMapper;
import com.educatalog.domain.Theme;
import com.educatalog.infra.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final EduMapper mapper;

    public ThemeResponse create(ThemeRequest request) {
        if (themeRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Theme with this name already exists");
        }
        Theme theme = mapper.toTheme(request);
        theme = themeRepository.save(theme);
        return mapper.toThemeResponse(theme);
    }
}
