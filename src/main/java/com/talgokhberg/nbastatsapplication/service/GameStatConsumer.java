package com.talgokhberg.nbastatsapplication.service;

import com.talgokhberg.nbastatsapplication.model.GameStat;
import com.talgokhberg.nbastatsapplication.repository.GameStatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class GameStatConsumer {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(GameStatConsumer.class);

    public GameStatConsumer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void consume(GameStat stat) {
        try {
            String sql = "INSERT INTO " + GameStatRepository.TABLE_NAME + " (" +
                    GameStatRepository.COLUMN_PLAYER_ID + ", " +
                    GameStatRepository.COLUMN_TEAM_ID + ", " +
                    GameStatRepository.COLUMN_GAME_ID + ", " +
                    GameStatRepository.COLUMN_POINTS + ", " +
                    GameStatRepository.COLUMN_REBOUNDS + ", " +
                    GameStatRepository.COLUMN_ASSISTS + ", " +
                    GameStatRepository.COLUMN_STEALS + ", " +
                    GameStatRepository.COLUMN_BLOCKS + ", " +
                    GameStatRepository.COLUMN_FOULS + ", " +
                    GameStatRepository.COLUMN_TURNOVERS + ", " +
                    GameStatRepository.COLUMN_MINUTES_PLAYED + ") " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(sql,
                    stat.getPlayerId(),
                    stat.getTeamId(),
                    stat.getGameId(),
                    stat.getPoints(),
                    stat.getRebounds(),
                    stat.getAssists(),
                    stat.getSteals(),
                    stat.getBlocks(),
                    stat.getFouls(),
                    stat.getTurnovers(),
                    stat.getMinutesPlayed()
            );
            logger.info("Game stat saved successfully: {}", stat);
        } catch (Exception e) {
            logger.error("Error processing game stat from Kafka", e);
        }
    }
}

