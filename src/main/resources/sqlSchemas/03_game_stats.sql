CREATE TABLE IF NOT EXISTS game_stats (
    id INT AUTO_INCREMENT PRIMARY KEY,
    player_id INT,
    team_id INT,
    game_id INT,
    points INT,
    rebounds INT,
    assists INT,
    steals INT,
    blocks INT,
    fouls INT CHECK (fouls <= 6),
    turnovers INT,
    minutes_played FLOAT CHECK (minutes_played BETWEEN 0 AND 48.0),
    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE
);