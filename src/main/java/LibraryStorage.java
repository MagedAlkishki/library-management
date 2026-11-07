import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

// Reads and writes the library as JSON so books (and who's borrowed what)
// survive between runs, without needing a real database for a console app.
public class LibraryStorage {

    public static void save(List<Book> books, String path) {
        JSONArray array = new JSONArray();
        for (Book book : books) {
            JSONObject entry = new JSONObject();
            entry.put("title", book.getTitle());
            entry.put("author", book.getAuthor());
            entry.put("genre", book.getGenre().name());
            entry.put("dueDate", book.getDueDate() == null ? JSONObject.NULL : book.getDueDate().toString());
            array.put(entry);
        }
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(array.toString(2));
        } catch (IOException e) {
            System.out.println("Could not save the library to disk (" + e.getMessage() + "); your changes this session may be lost.");
        }
    }

    public static List<Book> load(String path) {
        List<Book> books = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            return books;
        }
        try {
            String content = Files.readString(file.toPath());
            if (content.isBlank()) {
                return books;
            }
            JSONArray array = new JSONArray(content);
            for (int i = 0; i < array.length(); i++) {
                JSONObject entry = array.getJSONObject(i);
                String title = entry.optString("title", "").trim();
                if (title.isEmpty()) {
                    continue;
                }
                String author = entry.optString("author", "Unknown author");
                Genre genre = parseGenre(entry.optString("genre", "OTHER"));

                Book book = new Book(title, author, genre);
                if (!entry.isNull("dueDate")) {
                    String dueDateText = entry.optString("dueDate", null);
                    if (dueDateText != null) {
                        try {
                            book.restoreBorrowedState(LocalDate.parse(dueDateText));
                        } catch (DateTimeParseException e) {
                            // Unreadable date in the save file: leave the book as available
                            // rather than crashing the whole load over one bad entry.
                        }
                    }
                }
                books.add(book);
            }
        } catch (IOException e) {
            System.out.println("Could not read " + path + " (" + e.getMessage() + "); starting with an empty library.");
        } catch (JSONException e) {
            System.out.println("The saved library file looks corrupted; starting with an empty library.");
        }
        return books;
    }

    private static Genre parseGenre(String name) {
        try {
            return Genre.valueOf(name);
        } catch (IllegalArgumentException e) {
            return Genre.OTHER;
        }
    }
}
