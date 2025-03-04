package com.talgokhberg.nbastatsapplication;

import com.talgokhberg.nbastatsapplication.model.GameStat;
import com.talgokhberg.nbastatsapplication.service.GameStatConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.jdbc.core.JdbcTemplate;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "${kafka.topic}", brokerProperties = {"listeners=${kafka.listeners}", "port=${kafka.port}"})
public class GameStatIntegrationTest {

    @Value("${database.table.game_stats}")
    public String tableName;

    @Value("${kafka.topic}")
    public String topic;

    @Autowired
    private KafkaTemplate<String, GameStat> kafkaTemplate;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GameStatConsumer gameStatConsumer;

    private GameStat testStat;

    @BeforeEach
    void setup() {
        testStat = new GameStat(1, 2, 3, 4, 25, 8, 5, 3, 2, 4, 3, 35.0f);
    }

    @Test
    void testKafkaFlow_SuccessfulProcessing() throws Exception {
        // Send game stat to Kafka
        kafkaTemplate.send(topic, testStat);

        // Simulate a short wait for Kafka to process
        Thread.sleep(3000);

        // Capture the arguments passed to jdbcTemplate.update
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object[]> paramsCaptor = ArgumentCaptor.forClass(Object[].class);

        // Verify the consumer called the DB insert
        verify(jdbcTemplate, times(1)).update(sqlCaptor.capture(), paramsCaptor.capture());

        // Assert the query is correct
        String executedQuery = sqlCaptor.getValue();
        assertTrue(executedQuery.startsWith("INSERT INTO " + tableName));

        // Assert the inserted values match the test stat
        Object[] insertedValues = paramsCaptor.getValue();
        assertEquals(testStat.getPlayerId(), insertedValues[0]);
        assertEquals(testStat.getTeamId(), insertedValues[1]);
        assertEquals(testStat.getGameId(), insertedValues[2]);
        assertEquals(testStat.getPoints(), insertedValues[3]);
        assertEquals(testStat.getRebounds(), insertedValues[4]);
        assertEquals(testStat.getAssists(), insertedValues[5]);
        assertEquals(testStat.getSteals(), insertedValues[6]);
        assertEquals(testStat.getBlocks(), insertedValues[7]);
        assertEquals(testStat.getFouls(), insertedValues[8]);
        assertEquals(testStat.getTurnovers(), insertedValues[9]);
        assertEquals(testStat.getMinutesPlayed(), (float) insertedValues[10]);
    }
}

