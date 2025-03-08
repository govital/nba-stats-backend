package com.talgokhberg.nbastatsapplication.controller;

import com.talgokhberg.nbastatsapplication.service.PlayerService;
import com.talgokhberg.nbastatsapplication.model.Player;
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
@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private DataSource dataSource;

    @Test
    void testGetAllPlayers() throws Exception {
        // Mock player list
        List<Player> mockPlayers = Arrays.asList(
                new Player(1, "LeBron James", 23),
                new Player(2, "Stephen Curry", 30)
        );

        // Mock service response
        when(playerService.getAllPlayers()).thenReturn(mockPlayers);

        // Perform GET request and verify response
        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("LeBron James")))
                .andExpect(jsonPath("$[0].teamId", is(23)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Stephen Curry")))
                .andExpect(jsonPath("$[1].teamId", is(30)));

        // Verify playerService.getAllPlayers() was called once
        verify(playerService, times(1)).getAllPlayers();
    }
}
