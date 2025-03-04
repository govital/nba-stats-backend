# NBA Stats Backend

## Overview

This project is a **Spring Boot** backend for managing NBA player and team statistics. It utilizes **MySQL**, **Redis**, and **Kafka** for real-time data processing and caching.

## Features

- **Spring Boot + MySQL**: Stores player and team stats.
- **Redis Caching**: Speeds up fetching aggregated stats.
- **Kafka Streaming**: Asynchronous processing of game stats.
- **REST API**: Provides endpoints for retrieving and adding stats.
- **Docker Support**: Includes `docker-compose` setup for easy deployment.

## Technologies Used

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA (MySQL)**
- **Spring Data Redis**
- **Spring Kafka**
- **Docker & Docker Compose**
- **JUnit & Mockito** for testing

## Setup Instructions

### 1️⃣ Clone the Repository

```sh
git clone https://github.com/YOUR_USERNAME/nba-stats-backend.git
cd nba-stats-backend
```

### 2️⃣ Configure `application.properties`

Create `src/main/resources/application.properties` and set up:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/nba_stats
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Redis Configuration
spring.cache.type=redis
spring.redis.host=redis
spring.redis.port=6379

# Kafka Configuration
kafka.bootstrap-servers=localhost:9092
kafka.topic=game-stats-topic
kafka.group-id=game-stat-group
```

### 3️⃣ Run Services with Docker

Ensure you have **Docker** installed, then run:

```sh
docker-compose up --build -d
```

This will start **MySQL, Redis, Kafka, and Zookeeper**.

### 4️⃣ Run the Application

```sh
mvn clean install
mvn spring-boot:run
```

### 5️⃣ Test API Endpoints

#### Add a Team

```sh
curl -X POST http://localhost:8080/teams -H "Content-Type: application/json" -d '{"name": "Lakers"}'
```

#### Add a Player

```sh
curl -X POST http://localhost:8080/players -H "Content-Type: application/json" -d '{"name": "LeBron James", "teamId": 1}'
```

#### Log Game Stats (Kafka Processing)

```sh
curl -X POST http://localhost:8080/stats -H "Content-Type: application/json" -d '{
  "playerId": 1,
  "teamId": 1,
  "gameId": 101,
  "points": 30,
  "rebounds": 10,
  "assists": 5,
  "steals": 2,
  "blocks": 1,
  "fouls": 2,
  "turnovers": 3,
  "minutesPlayed": 38.5
}'
```

#### Fetch Player Stats

```sh
curl -X GET http://localhost:8080/stats/player/1
```

## Running Tests

Run unit tests with:

```sh
mvn test
```

## Deployment

To deploy using Docker:

```sh
docker build -t nba-stats-backend .
docker run -p 8080:8080 nba-stats-backend
```

## Contributing

1. Fork the repo.
2. Create a new branch.
3. Commit changes.
4. Push to GitHub and create a PR.

## License

MIT License

