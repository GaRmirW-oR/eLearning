package com.educatalog.infra;

import com.educatalog.domain.Lesson;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    @Override
    @EntityGraph(attributePaths = "theme")
    List<Lesson> findAll();
}
