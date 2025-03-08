package com.talgokhberg.nbastatsapplication.service;

import com.talgokhberg.nbastatsapplication.model.Team;
import com.talgokhberg.nbastatsapplication.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    void testGetAllTeams() {
        // Mock team list
        List<Team> mockTeams = Arrays.asList(
                new Team(1, "Los Angeles Lakers"),
                new Team(2, "Golden State Warriors")
        );

        // Mock repository response
        when(teamRepository.getAllTeams()).thenReturn(mockTeams);

        // Call service method
        List<Team> result = teamService.getAllTeams();

        // Verify result
        assertEquals(2, result.size());
        assertEquals("Los Angeles Lakers", result.get(0).getName());
        assertEquals("Golden State Warriors", result.get(1).getName());

        // Verify repository method was called once
        verify(teamRepository, times(1)).getAllTeams();
    }

    @Test
    void testGetTeamIdByName_Found() {
        // Mock repository response
        when(teamRepository.getTeamIdByName("Los Angeles Lakers")).thenReturn(1);

        // Call service method
        Integer result = teamService.getTeamIdByName("Los Angeles Lakers");

        // Verify result
        assertNotNull(result);
        assertEquals(1, result);

        // Verify repository method was called once
        verify(teamRepository, times(1)).getTeamIdByName("Los Angeles Lakers");
    }

    @Test
    void testGetTeamIdByName_NotFound() {
        // Mock repository response for a non-existing team
        when(teamRepository.getTeamIdByName("Unknown Team")).thenReturn(null);

        // Call service method
        Integer result = teamService.getTeamIdByName("Unknown Team");

        // Verify result
        assertNull(result);

        // Verify repository method was called once
        verify(teamRepository, times(1)).getTeamIdByName("Unknown Team");
    }
}
