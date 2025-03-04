package com.talgokhberg.nbastatsapplication.service;

import com.talgokhberg.nbastatsapplication.model.Player;
import com.talgokhberg.nbastatsapplication.repository.PlayerRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    public static final String PLAYERS_CACHE = "players";
    public static final String PLAYER_CACHE = "player";
    public static final String PLAYER_CACHE_KEY = "#id";

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public int addPlayer(Player player) {
        return playerRepository.addPlayer(player);
    }

    @Cacheable(value = PLAYERS_CACHE)
    public List<Player> getAllPlayers() {
        return playerRepository.getAllPlayers();
    }

    @Cacheable(value = PLAYER_CACHE, key = PLAYER_CACHE_KEY)
    public Player getPlayerById(int id) {
        return playerRepository.getPlayerById(id);
    }
}
