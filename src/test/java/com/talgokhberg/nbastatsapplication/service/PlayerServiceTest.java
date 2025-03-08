package com.talgokhberg.nbastatsapplication.service;

import com.talgokhberg.nbastatsapplication.model.Player;
import com.talgokhberg.nbastatsapplication.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void testGetAllPlayers() {
        // Mock player list
        List<Player> mockPlayers = Arrays.asList(
                new Player(1, "LeBron James", 23),
                new Player(2, "Stephen Curry", 30)
        );

        // Mock repository response
        when(playerRepository.getAllPlayers()).thenReturn(mockPlayers);

        // Call service method
        List<Player> result = playerService.getAllPlayers();

        // Verify result
        assertEquals(2, result.size());
        assertEquals("LeBron James", result.get(0).getName());
        assertEquals(23, result.get(0).getTeamId());
        assertEquals("Stephen Curry", result.get(1).getName());
        assertEquals(30, result.get(1).getTeamId());

        // Verify repository method was called once
        verify(playerRepository, times(1)).getAllPlayers();
    }

    @Test
    void testGetPlayerById_Found() {
        // Mock player
        Player mockPlayer = new Player(1, "LeBron James", 23);

        // Mock repository response
        when(playerRepository.getPlayerById(1)).thenReturn(mockPlayer);

        // Call service method
        Player result = playerService.getPlayerById(1);

        // Verify result
        assertEquals("LeBron James", result.getName());
        assertEquals(23, result.getTeamId());

        // Verify repository method was called once
        verify(playerRepository, times(1)).getPlayerById(1);
    }

    @Test
    void testGetPlayerById_NotFound() {
        // Mock repository response for non-existing player
        when(playerRepository.getPlayerById(99)).thenReturn(null);

        // Call service method
        Player result= playerService.getPlayerById(99);

        // Verify result
        assertNull(result);

        // Verify repository method was called once
        verify(playerRepository, times(1)).getPlayerById(99);
    }
}

