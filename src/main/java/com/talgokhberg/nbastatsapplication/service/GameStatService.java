package com.talgokhberg.nbastatsapplication.service;

import com.talgokhberg.nbastatsapplication.model.GameStat;
import com.talgokhberg.nbastatsapplication.repository.GameStatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GameStatService {
    private static final Logger logger = LoggerFactory.getLogger(GameStatService.class);
    private final GameStatRepository gameStatRepository;
    public static final String PLAYER_STATS_CACHE = "playerStats";
    public static final String PLAYER_STATS_CACHE_KEY = "#playerId";
    public static final String TEAM_STATS_CACHE = "teamStats";
    public static final String TEAM_STATS_CACHE_KEY = "#teamId";
    public static final String PLAYER_SEASON_AVG_CACHE = "playerSeasonAvg";
    public static final String TEAM_SEASON_AVG_CACHE = "teamSeasonAvg";

    public GameStatService(GameStatRepository gameStatRepository) {
        this.gameStatRepository = gameStatRepository;
    }

    @CacheEvict(value = {PLAYER_STATS_CACHE, TEAM_STATS_CACHE, PLAYER_SEASON_AVG_CACHE, TEAM_SEASON_AVG_CACHE}, allEntries = true)
    public void logGameStat(GameStat gameStat) {
        gameStatRepository.addGameStat(gameStat);
    }

    @Cacheable(value = PLAYER_STATS_CACHE, key = PLAYER_STATS_CACHE_KEY)
    public List<GameStat> getStatsByPlayer(int playerId) {
        logger.info("Fetching Stats By Player for player ID: {} from DATABASE", playerId);
        return gameStatRepository.getStatsByPlayerId(playerId);
    }

    @Cacheable(value = TEAM_STATS_CACHE, key = TEAM_STATS_CACHE_KEY)
    public List<GameStat> getStatsByTeam(int teamId) {
        logger.info("Fetching Stats By Team for Team ID: {} from DATABASE", teamId);
        return gameStatRepository.getStatsByTeamId(teamId);
    }

    @Cacheable(value = PLAYER_SEASON_AVG_CACHE, key = PLAYER_STATS_CACHE_KEY)
    public Map<String, Double> getPlayerSeasonAverage(int playerId) {
        logger.info("Fetching Player Season Average for player ID: {} from DATABASE", playerId);
        return gameStatRepository.getPlayerSeasonAverage(playerId);
    }

    @Cacheable(value = TEAM_SEASON_AVG_CACHE, key = TEAM_STATS_CACHE_KEY)
    public Map<String, Double> getTeamSeasonAverage(int teamId) {
        logger.info("Fetching Team Season Average for Team ID: {} from DATABASE", teamId);
        return gameStatRepository.getTeamSeasonAverage(teamId);
    }
}
