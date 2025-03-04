package com.talgokhberg.nbastatsapplication.service;

import com.talgokhberg.nbastatsapplication.model.GameStat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GameStatConsumerTest {

    private GameStatConsumer gameStatConsumer;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private Logger logger;

    private GameStat testStat;

    @BeforeEach
    void setup() {
        jdbcTemplate = mock(JdbcTemplate.class);
        gameStatConsumer = new GameStatConsumer(jdbcTemplate);
        testStat = new GameStat(1, 2, 3, 4, 25, 8, 5, 3, 2, 4, 3, 35.0f);
    }

    @Test
    void testConsumeGameStat_Success() {
        // Fix: Return 1 (indicating 1 row affected) instead of doNothing()
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Call the consumer method
        gameStatConsumer.consume(testStat);

        // Capture the arguments passed to jdbcTemplate.update
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object[]> paramsCaptor = ArgumentCaptor.forClass(Object[].class);

        verify(jdbcTemplate, times(1)).update(sqlCaptor.capture(), paramsCaptor.capture());

        // Ensure the correct SQL statement was executed
        String executedQuery = sqlCaptor.getValue();
        assertTrue(executedQuery.startsWith("INSERT INTO"));

        // Assert the values match
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

    @Test
    void testConsumeGameStat_DatabaseFailure() {
        // Fix: Simulate a database failure by throwing an exception
        doThrow(new RuntimeException("Database insert error"))
                .when(jdbcTemplate)
                .update(anyString(), any(Object[].class));

        // Call the consumer method
        gameStatConsumer.consume(testStat);

        // Ensure update was attempted even though it failed
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }
}
