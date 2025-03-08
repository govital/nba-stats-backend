FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/nba-stats.jar nba-stats.jar

EXPOSE 8080 5005

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "nba-stats.jar"]
