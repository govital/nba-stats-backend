package com.talgokhberg.nbastatsapplication.repository;

import com.talgokhberg.nbastatsapplication.model.GameStat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GameStatRepository {
    private final JdbcTemplate jdbcTemplate;
    private final KafkaTemplate<String, GameStat> kafkaTemplate;
    private final String kafkaTopic;

    public static final String TABLE_NAME = "game_stats";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PLAYER_ID = "player_id";
    public static final String COLUMN_TEAM_ID = "team_id";
    public static final String COLUMN_GAME_ID = "game_id";
    public static final String COLUMN_POINTS = "points";
    public static final String COLUMN_REBOUNDS = "rebounds";
    public static final String COLUMN_ASSISTS = "assists";
    public static final String COLUMN_STEALS = "steals";
    public static final String COLUMN_BLOCKS = "blocks";
    public static final String COLUMN_FOULS = "fouls";
    public static final String COLUMN_TURNOVERS = "turnovers";
    public static final String COLUMN_MINUTES_PLAYED = "minutes_played";

    public static final String AVG_POINTS = "avg_points";
    public static final String AVG_REBOUNDS = "avg_rebounds";
    public static final String AVG_ASSISTS = "avg_assists";
    public static final String AVG_STEALS = "avg_steals";
    public static final String AVG_BLOCKS = "avg_blocks";
    public static final String AVG_FOULS = "avg_fouls";
    public static final String AVG_TURNOVERS = "avg_turnovers";
    public static final String AVG_MINUTES_PLAYED = "avg_minutes";

    public GameStatRepository(JdbcTemplate jdbcTemplate, KafkaTemplate<String, GameStat> kafkaTemplate, @Value("${kafka.topic}") String kafkaTopic) {
        this.jdbcTemplate = jdbcTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopic = kafkaTopic;
    }

    public void addGameStat(GameStat stat) {
        kafkaTemplate.send(kafkaTopic, stat);
    }

    public List<GameStat> getStatsByPlayerId(int playerId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PLAYER_ID + " = ?";
        return jdbcTemplate.query(sql, gameStatRowMapper, playerId);
    }

    public List<GameStat> getStatsByTeamId(int teamId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TEAM_ID + " = ?";
        return jdbcTemplate.query(sql, gameStatRowMapper, teamId);
    }

    public Map<String, Double> getPlayerSeasonAverage(int playerId) {
        String sql = "SELECT AVG(" + COLUMN_POINTS + ") AS " + AVG_POINTS + ", AVG(" + COLUMN_REBOUNDS + ") AS " + AVG_REBOUNDS + ", AVG(" + COLUMN_ASSISTS + ") AS " + AVG_ASSISTS + ", AVG(" + COLUMN_STEALS + ") AS " + AVG_STEALS + ", AVG(" + COLUMN_BLOCKS + ") AS " + AVG_BLOCKS + ", AVG(" + COLUMN_FOULS + ") AS " + AVG_FOULS + ", AVG(" + COLUMN_TURNOVERS + ") AS " + AVG_TURNOVERS + ", AVG(" + COLUMN_MINUTES_PLAYED + ") AS " + AVG_MINUTES_PLAYED + " FROM " + TABLE_NAME + " WHERE " + COLUMN_PLAYER_ID + " = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{playerId}, (rs, rowNum) -> {
            Map<String, Double> averages = new HashMap<>();
            averages.put(COLUMN_POINTS, rs.getDouble(AVG_POINTS));
            averages.put(COLUMN_REBOUNDS, rs.getDouble(AVG_REBOUNDS));
            averages.put(COLUMN_ASSISTS, rs.getDouble(AVG_ASSISTS));
            averages.put(COLUMN_STEALS, rs.getDouble(AVG_STEALS));
            averages.put(COLUMN_BLOCKS, rs.getDouble(AVG_BLOCKS));
            averages.put(COLUMN_FOULS, rs.getDouble(AVG_FOULS));
            averages.put(COLUMN_TURNOVERS, rs.getDouble(AVG_TURNOVERS));
            averages.put(COLUMN_MINUTES_PLAYED, rs.getDouble(AVG_MINUTES_PLAYED));
            return averages;
        });
    }

    public Map<String, Double> getTeamSeasonAverage(int teamId) {
        String sql = "SELECT AVG(" + COLUMN_POINTS + ") AS " + AVG_POINTS + ", AVG(" + COLUMN_REBOUNDS + ") AS " + AVG_REBOUNDS + ", AVG(" + COLUMN_ASSISTS + ") AS " + AVG_ASSISTS + ", AVG(" + COLUMN_STEALS + ") AS " + AVG_STEALS + ", AVG(" + COLUMN_BLOCKS + ") AS " + AVG_BLOCKS + ", AVG(" + COLUMN_FOULS + ") AS " + AVG_FOULS + ", AVG(" + COLUMN_TURNOVERS + ") AS " + AVG_TURNOVERS + ", AVG(" + COLUMN_MINUTES_PLAYED + ") AS " + AVG_MINUTES_PLAYED + " FROM " + TABLE_NAME + " WHERE " + COLUMN_TEAM_ID + " = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{teamId}, (rs, rowNum) -> {
            Map<String, Double> averages = new HashMap<>();
            averages.put(COLUMN_POINTS, rs.getDouble(AVG_POINTS));
            averages.put(COLUMN_REBOUNDS, rs.getDouble(AVG_REBOUNDS));
            averages.put(COLUMN_ASSISTS, rs.getDouble(AVG_ASSISTS));
            averages.put(COLUMN_STEALS, rs.getDouble(AVG_STEALS));
            averages.put(COLUMN_BLOCKS, rs.getDouble(AVG_BLOCKS));
            averages.put(COLUMN_FOULS, rs.getDouble(AVG_FOULS));
            averages.put(COLUMN_TURNOVERS, rs.getDouble(AVG_TURNOVERS));
            averages.put(COLUMN_MINUTES_PLAYED, rs.getDouble(AVG_MINUTES_PLAYED));
            return averages;
        });
    }

    private final RowMapper<GameStat> gameStatRowMapper = (rs, rowNum) -> new GameStat(
            rs.getInt(COLUMN_ID),
            rs.getInt(COLUMN_PLAYER_ID),
            rs.getInt(COLUMN_TEAM_ID),
            rs.getInt(COLUMN_GAME_ID),
            rs.getInt(COLUMN_POINTS),
            rs.getInt(COLUMN_REBOUNDS),
            rs.getInt(COLUMN_ASSISTS),
            rs.getInt(COLUMN_STEALS),
            rs.getInt(COLUMN_BLOCKS),
            rs.getInt(COLUMN_FOULS),
            rs.getInt(COLUMN_TURNOVERS),
            rs.getFloat(COLUMN_MINUTES_PLAYED)
    );
}
