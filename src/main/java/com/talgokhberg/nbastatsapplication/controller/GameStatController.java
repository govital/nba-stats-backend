package com.talgokhberg.nbastatsapplication.controller;

import com.talgokhberg.nbastatsapplication.model.GameStat;
import com.talgokhberg.nbastatsapplication.service.GameStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stats")
public class GameStatController {
    private static final Logger logger = LoggerFactory.getLogger(GameStatController.class);
    private final GameStatService gameStatService;

    public GameStatController(GameStatService gameStatService) {
        this.gameStatService = gameStatService;
    }

    @PostMapping
    public ResponseEntity<?> logGameStat(@RequestBody GameStat gameStat) {
        try {
            validateStat(gameStat);
            gameStatService.logGameStat(gameStat);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            logger.info("GameStat Validation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error logging game stat for player ID: {}", gameStat.getPlayerId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void validateStat(GameStat stat) {
        if (stat.getFouls() < 0 || stat.getFouls() > 6) {
            throw new IllegalArgumentException("Fouls must be between 0 and 6");
        }
        if (stat.getMinutesPlayed() < 0.0 || stat.getMinutesPlayed() > 48.0) {
            throw new IllegalArgumentException("Minutes played must be between 0.0 and 48.0");
        }
        if (stat.getPoints() < 0 || stat.getRebounds() < 0 || stat.getAssists() < 0 || stat.getSteals() < 0 || stat.getBlocks() < 0 || stat.getTurnovers() < 0) {
            throw new IllegalArgumentException("Statistics cannot be negative");
        }
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<?> getStatsByPlayer(@PathVariable int playerId) {
        try {
            List<GameStat> stats = gameStatService.getStatsByPlayer(playerId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error retrieving stats for player ID: {}", playerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> getStatsByTeam(@PathVariable int teamId) {
        try {
            List<GameStat> stats = gameStatService.getStatsByTeam(teamId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error retrieving stats for team ID: {}", teamId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

