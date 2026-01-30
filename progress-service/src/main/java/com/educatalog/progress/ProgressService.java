package com.educatalog.progress;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final CatalogClient catalogClient;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "course-created", groupId = "progress-group")
    public void consume(String message) {
        try {
            System.out.println("Received message: " + message);
            JsonNode node = objectMapper.readTree(message);
            String idStr = node.get("id").asText();
            UUID courseId = UUID.fromString(idStr);

            if (progressRepository.findByCourseId(courseId).isEmpty()) {
                Progress p = new Progress();
                p.setCourseId(courseId);
                p.setStatus("NOT_STARTED");
                progressRepository.save(p);
                System.out.println("Progress created for course: " + courseId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Iterable<ProgressDto> getMyProgress() {
        return progressRepository.findAll().stream()
                .map(p -> {
                    String title = "Unknown";
                    try {
                        title = catalogClient.getLesson(p.getCourseId()).title();
                    } catch (Exception e) {
                        title = "Unavailable (Fallback)";
                    }
                    return new ProgressDto(p.getCourseId(), p.getStatus(), title);
                }).toList();
    }

    public record ProgressDto(UUID courseId, String status, String courseTitle) {
    }
}
