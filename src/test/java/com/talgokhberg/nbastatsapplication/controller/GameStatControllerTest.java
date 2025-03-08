package com.talgokhberg.nbastatsapplication.controller;

import com.talgokhberg.nbastatsapplication.model.GameStat;
import com.talgokhberg.nbastatsapplication.service.GameStatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GameStatController.class)
public class GameStatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameStatService gameStatService;

    @MockBean
    private DataSource dataSource;

    @Test
    void testGetPlayerStats() throws Exception {
        // Mock game stats
        List<GameStat> mockStats = Arrays.asList(
                new GameStat(1, 2, 1, 101, 30, 10, 5, 2, 1, 2, 3, 38.5f),
                new GameStat(2, 2, 1, 102, 25, 8, 7, 1, 0, 3, 4, 35.0f)
        );

        // Mock service response
        when(gameStatService.getStatsByPlayer(2)).thenReturn(mockStats);

        // Perform GET request and verify response
        mockMvc.perform(get("/stats/player/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].playerId", is(2)))
                .andExpect(jsonPath("$[0].teamId", is(1)))
                .andExpect(jsonPath("$[0].gameId", is(101)))
                .andExpect(jsonPath("$[0].points", is(30)))
                .andExpect(jsonPath("$[0].rebounds", is(10)))
                .andExpect(jsonPath("$[0].assists", is(5)))
                .andExpect(jsonPath("$[0].steals", is(2)))
                .andExpect(jsonPath("$[0].blocks", is(1)))
                .andExpect(jsonPath("$[0].fouls", is(2)))
                .andExpect(jsonPath("$[0].turnovers", is(3)))
                .andExpect(jsonPath("$[0].minutesPlayed", is(38.5)));

        // Verify gameStatService.getStatsByPlayerId() was called once
        verify(gameStatService, times(1)).getStatsByPlayer(2);
    }
}

