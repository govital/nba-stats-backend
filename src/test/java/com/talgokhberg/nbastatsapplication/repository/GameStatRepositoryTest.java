package com.talgokhberg.nbastatsapplication.repository;

import com.talgokhberg.nbastatsapplication.model.GameStat;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GameStatRepositoryTest {

    private final String topic = "game-stats-topic";

    private GameStatRepository gameStatRepository;

    @MockBean
    private KafkaTemplate<String, GameStat> kafkaTemplate;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    private GameStat testStat;

    @BeforeEach
    void setup() {
        kafkaTemplate = mock(KafkaTemplate.class);
        jdbcTemplate = mock(JdbcTemplate.class);
        gameStatRepository = new GameStatRepository(jdbcTemplate, kafkaTemplate, topic);
        testStat = new GameStat(1, 2, 3, 4, 25, 8, 5, 3, 2, 4, 3, 35.0f);
    }

    @Test
    void testSendGameStatToKafka_Success() {
        // Mock Kafka send() to return a completed future
        CompletableFuture<SendResult<String, GameStat>> future = CompletableFuture.completedFuture(
                new SendResult<>(new ProducerRecord<>("game-stats-topic", testStat),
                        new RecordMetadata(null, 0, 0, 0, 0L, 0, 0))
        );

        when(kafkaTemplate.send(anyString(), any(GameStat.class))).thenReturn(future);

        gameStatRepository.addGameStat(testStat);

        verify(kafkaTemplate, times(1)).send(anyString(), eq(testStat));
    }

    @Test
    void testSendGameStatToKafka_Failure() {
        // Simulate Kafka send failure
        CompletableFuture<SendResult<String, GameStat>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Kafka failure"));

        when(kafkaTemplate.send(anyString(), any(GameStat.class))).thenReturn(failedFuture);

        gameStatRepository.addGameStat(testStat);

        verify(kafkaTemplate, times(1)).send(anyString(), eq(testStat));
    }
}
