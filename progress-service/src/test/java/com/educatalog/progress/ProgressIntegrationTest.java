package com.educatalog.progress;

import com.educatalog.progress.CatalogClient;
import com.educatalog.progress.ProgressRepository;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("postgres")
class ProgressIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Container
    @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProgressRepository progressRepository;

    @MockBean
    private CatalogClient catalogClient;

    @Test
    void shouldCreateProgressOnKafkaEvent() throws Exception {
        // Prepare data
        UUID courseId = UUID.randomUUID();
        String messageRel = "{\"id\":\"" + courseId + "\", \"title\":\"Kafka 101\"}";

        // Mock Feign
        Mockito.when(catalogClient.getLesson(courseId))
                .thenReturn(new CatalogClient.LessonDto(courseId, "Kafka 101"));

        // Propagate event using a manual producer connected to container
        // Or simply inject KafkaTemplate if configured?
        // Check if basic KafkaTemplate is available.
        // Since we are in SpringBootTest, we can construct one or inject one.
        // Ideally we should use the one configured by Spring, but we need to ensure it
        // points to the container.
        // @ServiceConnection should handle it.

        // Wait, the service uses a Consumer. We need to be a Producer here.
        // Let's create a simple producer.
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(props);
        KafkaTemplate<String, String> template = new KafkaTemplate<>(producerFactory);

        template.send("course-created", messageRel).get(10, TimeUnit.SECONDS);

        // Verify async consumption
        await().atMost(10, TimeUnit.SECONDS).until(() -> progressRepository.findByCourseId(courseId).isPresent());

        // Verify API
        mockMvc.perform(get("/api/progress/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseTitle").value("Kafka 101"));
    }
}
