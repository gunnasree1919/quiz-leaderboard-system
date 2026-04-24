import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class QuizLeaderboard {

    private static final String BASE_URL =
            "https://devapigw.vidalhealthtpa.com/srm-quiz-task";

    private static final String REG_NO =
            "AP23110010375";

    public static void main(String[] args) {

        try {

            HttpClient client = HttpClient.newHttpClient();

            Map<String, Integer> leaderboardMap =
                    new HashMap<>();

            Set<String> processedEntries =
                    new HashSet<>();

            System.out.println("Polling API started...\n");

            // STEP 1 → Poll API 10 times
            for (int i = 0; i < 10; i++) {

                String url = BASE_URL
                        + "/quiz/messages?regNo="
                        + REG_NO
                        + "&poll="
                        + i;

                System.out.println("Calling poll = " + i);

                HttpRequest request =
                        HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .GET()
                                .build();

                HttpResponse<String> response =
                        client.send(
                                request,
                                HttpResponse.BodyHandlers.ofString()
                        );

                JSONObject jsonResponse =
                        new JSONObject(response.body());

                JSONArray events =
                        jsonResponse.getJSONArray("events");

                // STEP 2 → Process events
                for (int j = 0; j < events.length(); j++) {

                    JSONObject event =
                            events.getJSONObject(j);

                    String roundId =
                            event.getString("roundId");

                    String participant =
                            event.getString("participant");

                    int score =
                            event.getInt("score");

                    String uniqueKey =
                            roundId + "_" + participant;

                    // STEP 3 → Deduplication logic
                    if (!processedEntries.contains(uniqueKey)) {

                        processedEntries.add(uniqueKey);

                        leaderboardMap.put(
                                participant,
                                leaderboardMap.getOrDefault(
                                        participant,
                                        0
                                ) + score
                        );
                    }
                }

                // STEP 4 → Mandatory 5 second delay
                Thread.sleep(5000);
            }

            System.out.println("\nPolling completed successfully!\n");

            // STEP 5 → Convert map to list
            List<Map.Entry<String, Integer>> leaderboard =
                    new ArrayList<>(leaderboardMap.entrySet());

            // STEP 6 → Sort leaderboard descending
            leaderboard.sort((a, b) ->
                    b.getValue() - a.getValue());

            // STEP 7 → Compute total score
            int totalScore = leaderboard
                    .stream()
                    .mapToInt(Map.Entry::getValue)
                    .sum();

            JSONArray leaderboardArray =
                    new JSONArray();

            System.out.println("Final Leaderboard:");

            for (Map.Entry<String, Integer> entry : leaderboard) {

                System.out.println(
                        entry.getKey()
                                + " → "
                                + entry.getValue()
                );

                JSONObject obj = new JSONObject();

                obj.put("participant",
                        entry.getKey());

                obj.put("totalScore",
                        entry.getValue());

                leaderboardArray.put(obj);
            }

            System.out.println("\nTotal Score = "
                    + totalScore);

            // STEP 8 → Submit leaderboard
            JSONObject submitPayload =
                    new JSONObject();

            submitPayload.put("regNo", REG_NO);
            submitPayload.put("leaderboard",
                    leaderboardArray);

            HttpRequest submitRequest =
                    HttpRequest.newBuilder()
                            .uri(URI.create(
                                    BASE_URL
                                            + "/quiz/submit"
                            ))
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    submitPayload.toString()
                                            )
                            )
                            .build();

            HttpResponse<String> submitResponse =
                    client.send(
                            submitRequest,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println("\nSubmission Result:");

            System.out.println(
                    submitResponse.body()
            );

        } catch (Exception e) {

            System.out.println("Error occurred:");

            e.printStackTrace();
        }
    }
}