package com.talgokhberg.nbastatsapplication.service;

import com.talgokhberg.nbastatsapplication.model.GameStat;
import com.talgokhberg.nbastatsapplication.repository.GameStatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameStatServiceTest {

    @Mock
    private GameStatRepository gameStatRepository;

    @InjectMocks
    private GameStatService gameStatService;

    @Test
    void testGetStatsByPlayerId() {
        // Mock game stats
        List<GameStat> mockStats = Arrays.asList(
                new GameStat(1, 2, 1, 101, 30, 10, 5, 2, 1, 2, 3, 38.5f),
                new GameStat(2, 2, 1, 102, 25, 8, 7, 1, 0, 3, 4, 35.0f)
        );

        // Mock repository response
        when(gameStatRepository.getStatsByPlayerId(2)).thenReturn(mockStats);

        // Call service method
        List<GameStat> result = gameStatService.getStatsByPlayer(2);

        // Verify result
        assertEquals(2, result.size());
        assertEquals(30, result.get(0).getPoints());
        assertEquals(25, result.get(1).getPoints());

        // Verify repository method was called once
        verify(gameStatRepository, times(1)).getStatsByPlayerId(2);
    }

    @Test
    void testGetStatsByTeamId() {
        // Mock game stats
        List<GameStat> mockStats = Arrays.asList(
                new GameStat(1, 2, 1, 101, 30, 10, 5, 2, 1, 2, 3, 38.5f),
                new GameStat(2, 3, 1, 102, 20, 6, 4, 0, 2, 1, 2, 30.0f)
        );

        // Mock repository response
        when(gameStatRepository.getStatsByTeamId(1)).thenReturn(mockStats);

        // Call service method
        List<GameStat> result = gameStatService.getStatsByTeam(1);

        // Verify result
        assertEquals(2, result.size());
        assertEquals(30, result.get(0).getPoints());
        assertEquals(20, result.get(1).getPoints());

        // Verify repository method was called once
        verify(gameStatRepository, times(1)).getStatsByTeamId(1);
    }

    @Test
    void testGetPlayerSeasonAverage() {
        // Mock season averages
        Map<String, Double> mockAverages = Map.of(
                "points", 25.5,
                "rebounds", 8.2,
                "assists", 6.4
        );

        // Mock repository response
        when(gameStatRepository.getPlayerSeasonAverage(2)).thenReturn(mockAverages);

        // Call service method
        Map<String, Double> result = gameStatService.getPlayerSeasonAverage(2);

        // Verify result
        assertEquals(25.5, result.get("points"));
        assertEquals(8.2, result.get("rebounds"));
        assertEquals(6.4, result.get("assists"));

        // Verify repository method was called once
        verify(gameStatRepository, times(1)).getPlayerSeasonAverage(2);
    }

    @Test
    void testGetTeamSeasonAverage() {
        // Mock season averages
        Map<String, Double> mockAverages = Map.of(
                "points", 110.0,
                "rebounds", 45.6,
                "assists", 24.8
        );

        // Mock repository response
        when(gameStatRepository.getTeamSeasonAverage(1)).thenReturn(mockAverages);

        // Call service method
        Map<String, Double> result = gameStatService.getTeamSeasonAverage(1);

        // Verify result
        assertEquals(110.0, result.get("points"));
        assertEquals(45.6, result.get("rebounds"));
        assertEquals(24.8, result.get("assists"));

        // Verify repository method was called once
        verify(gameStatRepository, times(1)).getTeamSeasonAverage(1);
    }
}

