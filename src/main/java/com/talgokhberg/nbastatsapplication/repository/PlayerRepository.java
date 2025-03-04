package com.talgokhberg.nbastatsapplication.repository;

import com.talgokhberg.nbastatsapplication.model.Player;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerRepository {
    private final JdbcTemplate jdbcTemplate;

    public PlayerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Player> getAllPlayers() {
        return jdbcTemplate.query("SELECT * FROM players", playerRowMapper);
    }

    public Player getPlayerById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM players WHERE id = ?", playerRowMapper, id);
    }

    public int addPlayer(Player player) {
        return jdbcTemplate.update("INSERT INTO players (name, team_id) VALUES (?, ?)", player.getName(), player.getTeamId());
    }

    private final RowMapper<Player> playerRowMapper = (rs, rowNum) -> new Player(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("team_id")
    );
}
