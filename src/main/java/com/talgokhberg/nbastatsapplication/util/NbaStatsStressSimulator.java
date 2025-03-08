package com.talgokhberg.nbastatsapplication.util;

import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * before using this - you must create 2 teams with 1 player in each and then update the values of:
 * playerId1 | playerId2 | player1TeamId | player2TeamId
 */
public class NbaStatsStressSimulator {
    private static final String BASE_URL = "http://localhost:8080";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Random random = new Random();

    // UPDATE VALUES BELOW TO PLAYERS AND TEAMS CREASTED BY YOU
    private static final int playerId1 = 2;
    private static final int playerId2 = 14;
    private static final int player1TeamId = 1;
    private static final int player2TeamId =  5;


    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(50); // 50 concurrent threads



        // Stress Test: Send many concurrent requests
        for (int i = 0; i < 1; i++) {
            int playerId = (random.nextBoolean()) ? playerId1 : playerId2;
            int teamId = (playerId == playerId1) ? player1TeamId : player2TeamId;

            executor.submit(() -> logGameStats(playerId, teamId));
            executor.submit(() -> getPlayerAverage(playerId));
            executor.submit(() -> getTeamAverage(teamId));
        }

        // Shutdown executor and wait for all tasks to complete
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
    }

    private static void createTeam(String name) {
        String json = String.format("{\"name\": \"%s\"}", name);
        sendPostRequest("/teams", json);
    }

    private static void createPlayer(String name, int teamId) {
        String json = String.format("{\"name\": \"%s\", \"teamId\": %d}", name, teamId);
        sendPostRequest("/players", json);
    }

    private static void logGameStats(int playerId, int teamId) {
        String json = String.format(
                "{\"playerId\": %d, \"teamId\": %d, \"gameId\": %d, \"points\": %d, \"rebounds\": %d, \"assists\": %d, \"steals\": %d, \"blocks\": %d, \"fouls\": %d, \"turnovers\": %d, \"minutesPlayed\": %.1f}",
                playerId, teamId, random.nextInt(1000),
                random.nextInt(40), random.nextInt(15), random.nextInt(10),
                random.nextInt(5), random.nextInt(5), random.nextInt(5), random.nextInt(5),
                20 + random.nextDouble() * 20
        );
        sendPostRequest("/stats", json);
    }

    private static void getPlayerAverage(int playerId) {
        sendGetRequest("/stats/player/"+playerId+"/average");
    }

    private static void getTeamAverage(int teamId) {
        sendGetRequest("/stats/team/"+teamId+"/average");
    }

    private static void sendPostRequest(String endpoint, String json) {
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string(); //
                System.out.println("POST " + endpoint + " SUCCESS: " + responseBody);
            } else if (response.code() == 409) {
                System.err.println("POST " + endpoint + " FAILED: Team already exists (409 Conflict)");
            } else {
                System.err.println("POST " + endpoint + " FAILED: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void sendGetRequest(String endpoint) {
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("GET " + endpoint + " SUCCESS: " + response.body().string());
            } else {
                System.err.println("GET " + endpoint + " FAILED: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int extractId(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            return json.getInt("id"); // Ensure the response contains an "id" field
        } catch (Exception e) {
            System.err.println("Failed to extract ID: " + jsonResponse);
            return -1;
        }
    }
}

