package com.talgokhberg.nbastatsapplication.repository;

import com.talgokhberg.nbastatsapplication.model.Team;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TeamRepository {
    private final JdbcTemplate jdbcTemplate;

    public TeamRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Team> getAllTeams() {
        return jdbcTemplate.query("SELECT * FROM teams", teamRowMapper);
    }

    public Team getTeamById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM teams WHERE id = ?", teamRowMapper, id);
    }

    public Map<String, Object> addTeam(Team team) {
        jdbcTemplate.update("INSERT INTO teams (name) VALUES (?)", team.getName());
        Integer teamId = jdbcTemplate.queryForObject("SELECT id FROM teams WHERE name = ?", new Object[]{team.getName()}, Integer.class);
        Map<String, Object> response = new HashMap<>();
        response.put("id", teamId);
        response.put("name", team.getName());
        return response;
    }

    public Integer getTeamIdByName(String name) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id FROM teams WHERE name = ?",
                    new Object[]{name},
                    Integer.class
            );
        } catch (Exception e) {
            return null; // Return null if team does not exist
        }
    }

    private final RowMapper<Team> teamRowMapper = (rs, rowNum) -> new Team(
            rs.getInt("id"),
            rs.getString("name")
    );
}
