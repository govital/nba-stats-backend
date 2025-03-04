package com.talgokhberg.nbastatsapplication.controller;

import com.talgokhberg.nbastatsapplication.model.Team;
import com.talgokhberg.nbastatsapplication.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teams")
public class TeamController {
    private static final Logger logger = LoggerFactory.getLogger(TeamController.class);
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTeams() {
        try {
            List<Team> teams = teamService.getAllTeams();
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            logger.error("Error retrieving teams", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeamById(@PathVariable int id) {
        try {
            Team team = teamService.getTeamById(id);
            return ResponseEntity.ok(team);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Team not found with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error retrieving team with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addTeam(@RequestBody Team team) {
        try {
            Map<String, Object> teamData = teamService.addTeam(team);
            return ResponseEntity.status(HttpStatus.CREATED).body(teamData);
        } catch (DataIntegrityViolationException e) {
            logger.info("Team already exists: {}", team.getName(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            logger.error("Error adding team: {}", team.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/id/{name}")
    public ResponseEntity<?> getTeamIdByName(@PathVariable String name) {
        try {
            Integer teamId = teamService.getTeamIdByName(name);
            return ResponseEntity.ok(teamId);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error retrieving team ID for name: {}", name, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
