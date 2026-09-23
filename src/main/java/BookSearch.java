import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BookSearch {

    public static class Result {
        private final String title;
        private final String author;
        private final int year;

        public Result(String title, String author, int year) {
            this.title = title;
            this.author = author;
            this.year = year;
        }

        public String getTitle() { return title; }
        public String getAuthor() { return author; }

        @Override
        public String toString() {
            String yearText = year > 0 ? " (" + year + ")" : "";
            return title + " by " + author + yearText;
        }
    }

    public static List<Result> search(String query) {
        List<Result> results = new ArrayList<>();
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = "https://openlibrary.org/search.json?q=" + encodedQuery + "&limit=5";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Open Library returned status " + response.statusCode() + ": " + response.body());
                return results;
            }

            JSONObject json = new JSONObject(response.body());

            if (!json.has("docs")) {
                System.out.println("Unexpected response from Open Library:");
                System.out.println(response.body());
                return results;
            }

            JSONArray docs = json.getJSONArray("docs");

            for (int i = 0; i < docs.length(); i++) {
                JSONObject doc = docs.getJSONObject(i);
                String title = doc.optString("title", "Unknown title");

                String author = "Unknown author";
                if (doc.has("author_name")) {
                    JSONArray authors = doc.getJSONArray("author_name");
                    if (authors.length() > 0) {
                        author = authors.getString(0);
                    }
                }

                int year = doc.optInt("first_publish_year", 0);
                results.add(new Result(title, author, year));
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Could not reach Open Library: " + e.getMessage());
        }
        return results;
    }
}