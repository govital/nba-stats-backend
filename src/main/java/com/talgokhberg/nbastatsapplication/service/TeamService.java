package com.talgokhberg.nbastatsapplication.service;

import com.talgokhberg.nbastatsapplication.model.Team;
import com.talgokhberg.nbastatsapplication.repository.TeamRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    public static final String TEAM_CACHE = "team";
    public static final String TEAM_CACHE_KEY = "#id";
    public static final String TEAMS_CACHE = "teams";
    public static final String TEAM_ID_CACHE = "teamId";
    public static final String TEAM_ID_CACHE_KEY = "#name";


    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Map<String, Object> addTeam(Team team) {
        return teamRepository.addTeam(team);
    }

    @Cacheable(value = TEAM_CACHE, key = TEAM_CACHE_KEY)
    public Team getTeamById(int id) {
        return teamRepository.getTeamById(id);
    }

    @Cacheable(value = TEAMS_CACHE)
    public List<Team> getAllTeams() {
        return teamRepository.getAllTeams();
    }

    @Cacheable(value = TEAM_ID_CACHE, key = TEAM_ID_CACHE_KEY)
    public Integer getTeamIdByName(String name) {
        return teamRepository.getTeamIdByName(name);
    }
}
