package com.talgokhberg.nbastatsapplication.controller;

import com.talgokhberg.nbastatsapplication.service.TeamService;
import com.talgokhberg.nbastatsapplication.model.Team;
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
@WebMvcTest(TeamController.class)
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamService teamService;

    @MockBean
    private DataSource dataSource;

    @Test
    void testGetAllTeams() throws Exception {
        // Mock team list
        List<Team> mockTeams = Arrays.asList(
                new Team(1, "Los Angeles Lakers"),
                new Team(2, "Golden State Warriors")
        );

        // Mock service response
        when(teamService.getAllTeams()).thenReturn(mockTeams);

        // Perform GET request and verify response
        mockMvc.perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Los Angeles Lakers")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Golden State Warriors")));

        // Verify teamService.getAllTeams() was called once
        verify(teamService, times(1)).getAllTeams();
    }
}

