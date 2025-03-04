# NBA Stats Backend

## Overview
This project is a Spring Boot backend for logging and retrieving NBA player statistics. It supports logging player game stats, retrieving individual and team averages, and running in a Dockerized environment.

## Features
- RESTful API to log player statistics
- Aggregate season statistics per player and team
- MySQL database integration
- Containerized with Docker and Docker Compose

## Installation & Running

### Prerequisites
- Java 17
- Maven
- Docker & Docker Compose

### Build & Run Locally
1. Clone the repository:
   ```sh
   git clone <repo_url>
   cd nba-stats-backend
   ```
2. Build the project:
   ```sh
   mvn clean package
   ```
3. Run MySQL locally:
   ```sh
   docker-compose up -d mysql
   ```
4. Start the Spring Boot application:
   ```sh
   java -jar target/nba-stats.jar
   ```

### Running with Docker Compose
1. Build and run the services:
   ```sh
   docker-compose up --build
   ```
2. The application will be available at `http://localhost:8080`

## API Endpoints
### Players
- `POST /players` - Add a new player
- `GET /players` - Retrieve all players
- `GET /players/{id}` - Retrieve a specific player

### Teams
- `POST /teams` - Add a new team
- `GET /teams` - Retrieve all teams
- `GET /teams/{id}` - Retrieve a specific team

### Game Stats
- `POST /stats` - Log a player's game stats
- `GET /stats/player/{playerId}` - Retrieve stats for a player
- `GET /stats/team/{teamId}` - Retrieve stats for a team
- `GET /stats/player/{playerId}/average` - Retrieve season averages for a player
- `GET /stats/team/{teamId}/average` - Retrieve season averages for a team

## Deployment on AWS
- Use **Amazon RDS** for MySQL database.
- Deploy the Spring Boot app on **AWS ECS** with Fargate.
- Use **AWS ALB (Application Load Balancer)** for routing traffic.

## License
MIT License
