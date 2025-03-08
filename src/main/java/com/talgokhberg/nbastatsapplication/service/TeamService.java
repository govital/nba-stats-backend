package com.talgokhberg.nbastatsapplication.service;

import com.talgokhberg.nbastatsapplication.model.Team;
import com.talgokhberg.nbastatsapplication.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TeamService {
    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);
    private final TeamRepository teamRepository;
    public static final String TEAM_CACHE = "team";
    public static final String TEAM_CACHE_KEY = "#id";
    public static final String TEAMS_CACHE = "teams";
    public static final String TEAM_ID_CACHE = "teamId";
    public static final String TEAM_ID_CACHE_KEY = "#name";


    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @CacheEvict(value = TEAMS_CACHE, allEntries = true)
    public Map<String, Object> addTeam(Team team) {
        return teamRepository.addTeam(team);
    }

    @Cacheable(value = TEAM_CACHE, key = TEAM_CACHE_KEY)
    public Team getTeamById(int id) {
        logger.info("Fetching Team by ID: {} from DB", id);
        return teamRepository.getTeamById(id);
    }

    @Cacheable(value = TEAMS_CACHE)
    public List<Team> getAllTeams() {
        logger.info("Fetching all Teams from DB");
        return teamRepository.getAllTeams();
    }

    @Cacheable(value = TEAM_ID_CACHE, key = TEAM_ID_CACHE_KEY)
    public Integer getTeamIdByName(String name) {
        logger.info("Fetching Team by ID: {} from DB", name);
        return teamRepository.getTeamIdByName(name);
    }

    @CacheEvict(value = TEAMS_CACHE, allEntries = true)
    public boolean deleteTeam(int teamId) {
        int rowsAffected = teamRepository.deleteTeamById(teamId);
        if (rowsAffected > 0) {
            logger.info("Deleted team with ID: {}", teamId);
            return true;
        } else {
            logger.warn("Team ID {} not found", teamId);
            return false;
        }
    }
}
