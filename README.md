Here is a detailed documentation outlining the thought process, challenges, trade-offs, system design decisions, implementation considerations, and deployment strategy for NBA Stats API project.

# NBA Stats API - Design & Implementation Documentation
## Overview
The NBA Stats API is a scalable, high-performance backend built using Java + Spring Boot, designed to handle high throughput and real-time data availability. The system ingests, processes, and aggregates player game statistics, utilizing MySQL for storage, Redis for caching, and Kafka for event-driven processing.

### Thought Process & Architectural Decisions

#### System Architecture

The architecture follows a Clean Architecture approach, ensuring separation of concerns:
Controller Layer – Handles API requests & responses.
Service Layer – Business logic and caching strategies.
Repository Layer – Interacts with MySQL via JDBC.
Event Processing Layer – Uses Kafka to process game stats asynchronously.
Caching Layer – Uses Redis for fast retrieval of aggregated statistics.

Database Choice: MySQL
Why MySQL?
Relational structure fits well for team & player relationships.
ACID compliance ensures data integrity (important for player stats).
Foreign Key constraints maintain referential integrity.

#### Scalability Considerations

Kafka for Asynchronous Processing:
Logs game stats without blocking API requests.
Consumers process game stats in parallel, improving throughput.

Redis for Caching:
Frequently accessed data (team/player averages) are cached.
Reduces database load, improving response times.

#### Trade-offs & Challenges
Challenge Ensuring data consistency across MySQL & Redis
Solution  Used Cache Eviction Strategy (invalidate cache when data changes)
Trade-off Increased write complexity but faster reads

Challenge  Avoiding race conditions in Kafka processing
Solution   Implemented foreign key validation before inserting records
Trade-off  Slight increase in processing time per message

Challenge  Handling large-scale concurrent requests
Solution   Used Redis caching to reduce DB queries
Trade-off  Requires cache invalidation logic


#### Design & Implementation Considerations
Key Design Decisions
REST API with JSON responses → Easy for machine to machine integrations while maintaining human readability.
Kafka for Dead Letter Queue (DLQ) → Ensures error messages are not lost.
Containerized Deployment (Docker + Compose) → Simplifies scalability & portability.

#### Running the Project

Clone & Setup the Project
```sh
https://github.com/govital/nba-stats-backend.git
cd nba-stats-backend
```

##### Build the project:
```sh
mvn clean package
```

##### Start with Docker:
```sh
docker-compose up --build

stop:
docker-compose down
```

##### This starts:
Nba Stats App (Port: 8080)
MySQL (Port: 3306)
Redis (Port: 6379)
Kafka + Zookeeper (Ports: 9092, 2181)

#### Test API Endpoints
##### Add a Team:
```sh
curl -X POST http://localhost:8080/teams -H "Content-Type: application/json" -d '{"name": "Los Angeles Lakers"}'
```

##### get new team id from response:
```sh
{
    "name": "Los Angeles Lakers",
    "id": 1
}
```


##### Add a Player - use team id from response above (1 in this example):
```sh
curl -X POST http://localhost:8080/players -H "Content-Type: application/json" -d '{"name": "LeBron James", "teamId": 1}'
```

##### response is player id of created player:
```sh
1
```

##### Log Game Stats (use team id & player id from answers above (1 and 1)):

```sh
curl -X POST http://localhost:8080/stats -H "Content-Type: application/json" -d '{
  "playerId": 1, "teamId": 1, "gameId": 101, "points": 30, "rebounds": 10,
  "assists": 5, "steals": 2, "blocks": 1, "fouls": 2, "turnovers": 3, "minutesPlayed": 38.5
}'
```

#### Fetch Player Season Average (use created player id - 1 in this example)
```sh
curl -X GET http://localhost:8080/stats/player/1/average
```

#### Fetch Team Season Average (use created team id - 1 in this example)
```sh
curl -X GET http://localhost:8080/stats/team/1/average
```


### Listen on Kafka Topic
```sh 
docker exec -it kafka bash 
kafka-console-consumer --bootstrap-server kafka:9092 --topic game-stats-topic --from-beginning

Check Messages in DLQ:
kafka-console-consumer --bootstrap-server localhost:9092 --topic game-stats-dlq --from-beginning 
``` 

### See cached data in Redis (use created player id - 1 in this example):
```sh 
docker exec -it redis_cache redis-cli 
keys * 
get playerStats::1
``` 


### Deploying on AWS

To deploy this system on AWS, follow these steps:
#### Infrastructure Setup

Application Server:
Amazon EKS (Dockerized)

Database:
Amazon RDS (MySQL 8.0)

Cache:
Amazon ElastiCache (Redis)

Kafka:
Amazon MSK (Managed Kafka)

Deploy with AWS ECS (Dockerized)

##### Build & Push Docker Image:
```sh
docker build -t nba-stats-backend .
docker tag nba-stats-backend:latest <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/nba-stats-backend:latest
docker push <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/nba-stats-backend:latest
```

##### Deploy via ECS (Elastic Container Service):
Create an ECS Cluster
Deploy a Fargate Task with the Docker Image
Expose Port 8080 via Load Balancer

Use Amazon RDS for MySQL
Create RDS MySQL instance (db.t3.micro)

#### Update application.properties
```sh
spring.datasource.url=jdbc:mysql://nba-stats-db.cbxdbsomerandomid.us-east-1.rds.amazonaws.com:3306/nba_stats
spring.datasource.username=admin
spring.datasource.password=mysecurepassword
```

###### Set Up Kafka with Amazon MSK:
Create an MSK Cluster
Update Kafka Config in application.properties

##### properties
```sh
kafka.bootstrap-servers=b-1.nba-stats-msk.us-east-1.amazonaws.com:9092
```

### Autoscaling & Load Balancing

Use AWS Application Load Balancer (ALB) to route requests.
Enable Auto Scaling for ECS tasks to handle traffic spikes.

### Final Thoughts

#### Strengths of the Solution:
Highly Scalable (Kafka + Redis reduce load on MySQL)
Fault Tolerant (Kafka DLQ ensures no lost messages)
Cloud-Ready (Dockerized, AWS-compatible)

#### Future Enhancements:
AWS Lambda for real-time stat aggregation.
Prometheus + Grafana for monitoring Kafka processing.
Multi-Region Database Replication for high availability.

#### Conclusion:
This system is built to handle high traffic, process real-time player statistics, and be easily scalable using Kafka, Redis, and AWS services.

