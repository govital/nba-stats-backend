package com.talgokhberg.nbastatsapplication.model;

import java.io.Serializable;

public class GameStat implements Serializable { // ✅ Implements Serializable
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    private int id;
    private int playerId;
    private int teamId;
    private int gameId;
    private int points;
    private int rebounds;
    private int assists;
    private int steals;
    private int blocks;
    private int fouls;
    private int turnovers;
    private float minutesPlayed;

    public GameStat() {}

    public GameStat(int id, int playerId, int teamId, int gameId, int points, int rebounds, int assists,
                    int steals, int blocks, int fouls, int turnovers, float minutesPlayed) {
        this.id = id;
        this.playerId = playerId;
        this.teamId = teamId;
        this.gameId = gameId;
        this.points = points;
        this.rebounds = rebounds;
        this.assists = assists;
        this.steals = steals;
        this.blocks = blocks;
        this.fouls = fouls;
        this.turnovers = turnovers;
        this.minutesPlayed = minutesPlayed;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public int getGameId() { return gameId; }
    public void setGameId(int gameId) { this.gameId = gameId; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getRebounds() { return rebounds; }
    public void setRebounds(int rebounds) { this.rebounds = rebounds; }

    public int getAssists() { return assists; }
    public void setAssists(int assists) { this.assists = assists; }

    public int getSteals() { return steals; }
    public void setSteals(int steals) { this.steals = steals; }

    public int getBlocks() { return blocks; }
    public void setBlocks(int blocks) { this.blocks = blocks; }

    public int getFouls() { return fouls; }
    public void setFouls(int fouls) { this.fouls = fouls; }

    public int getTurnovers() { return turnovers; }
    public void setTurnovers(int turnovers) { this.turnovers = turnovers; }

    public float getMinutesPlayed() { return minutesPlayed; }
    public void setMinutesPlayed(float minutesPlayed) { this.minutesPlayed = minutesPlayed; }
}

