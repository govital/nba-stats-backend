package com.talgokhberg.nbastatsapplication.service;

import com.talgokhberg.nbastatsapplication.model.GameStat;
import com.talgokhberg.nbastatsapplication.repository.GameStatRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class GameStatConsumer {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(GameStatConsumer.class);
    private final KafkaTemplate<String, GameStat> kafkaTemplate;
    private final String dlqTopic;

    public GameStatConsumer(JdbcTemplate jdbcTemplate, KafkaTemplate<String, GameStat> kafkaTemplate,
                            @Value("${kafka.dlq-topic}") String dlqTopic) {
        this.jdbcTemplate = jdbcTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.dlqTopic = dlqTopic;
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void consume(ConsumerRecord<String, GameStat> record, Acknowledgment ack) {
        GameStat stat = record.value();
        try {

            // Ensure player exists before processing stats
            int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM players WHERE id = ?", Integer.class, stat.getPlayerId());
            if (count == 0) {
                logger.warn("Player with ID {} does not exist. Dropping stat entry.", stat.getPlayerId());
                ack.acknowledge(); //  Acknowledge to prevent infinite retries
                return;
            }

            // Ensure team exists before processing stats
            int teamCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM teams WHERE id = ?", Integer.class, stat.getTeamId());
            if (teamCount == 0) {
                logger.warn("Team with ID {} does not exist. Dropping stat entry.", stat.getTeamId());
                ack.acknowledge();
                return;
            }





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

            // Acknowledge successful processing
            ack.acknowledge();
            logger.info("Game stat saved successfully: {}", stat);
        } catch (Exception e) {
            // Send message to Dead Letter Queue
            kafkaTemplate.send(dlqTopic, stat);

            // Acknowledge the message to avoid infinite reprocessing
            ack.acknowledge();
            logger.error("Error processing game stat from Kafka. Sent to DLQ: {}", stat, e);
        }
    }
}
