package com.talgokhberg.nbastatsapplication.service;

import com.talgokhberg.nbastatsapplication.model.Player;
import com.talgokhberg.nbastatsapplication.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.talgokhberg.nbastatsapplication.service.TeamService.TEAMS_CACHE;

@Service
public class PlayerService {
    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);
    private final PlayerRepository playerRepository;
    public static final String PLAYERS_CACHE = "players";
    public static final String PLAYER_CACHE = "player";
    public static final String PLAYER_CACHE_KEY = "#id";

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @CacheEvict(value = PLAYERS_CACHE, allEntries = true)
    public int addPlayer(Player player) {
        return playerRepository.addPlayer(player);
    }

    @Cacheable(value = PLAYERS_CACHE)
    public List<Player> getAllPlayers() {
        logger.info("Fetching All Players from DATABASE");
        return playerRepository.getAllPlayers();
    }

    @Cacheable(value = PLAYER_CACHE, key = PLAYER_CACHE_KEY)
    public Player getPlayerById(int id) {
        logger.info("Fetching Player By Id from DATABASE for player id: {}", id);
        return playerRepository.getPlayerById(id);
    }

    @CacheEvict(value = PLAYERS_CACHE, allEntries = true)
    public boolean deletePlayer(int playerId) {
        int rowsAffected = playerRepository.deletePlayerById(playerId);
        if (rowsAffected > 0) {
            logger.info("Deleted player with ID: {}", playerId);
            return true;
        } else {
            logger.warn("Player ID {} not found", playerId);
            return false;
        }
    }
}
