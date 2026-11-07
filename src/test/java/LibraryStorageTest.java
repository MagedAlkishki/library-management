import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryStorageTest {

    private static final String TEST_FILE = "library-data-test.json";

    @AfterEach
    void cleanUp() {
        new File(TEST_FILE).delete();
    }

    @Test
    void loadOnMissingFileReturnsAnEmptyList() {
        assertTrue(LibraryStorage.load(TEST_FILE).isEmpty());
    }

    @Test
    void saveThenLoadRoundTripsAvailableAndBorrowedBooks() {
        Library library = new Library();
        library.addBook(new Book("Clean Code", "Robert C. Martin", Genre.PROGRAMMING));
        library.addBook(new Book("Dune", "Frank Herbert", Genre.FICTION));
        library.borrowBook("Dune");

        LibraryStorage.save(library.getAllBooks(), TEST_FILE);
        List<Book> loaded = LibraryStorage.load(TEST_FILE);

        assertEquals(2, loaded.size());

        Book cleanCode = loaded.stream().filter(b -> b.getTitle().equals("Clean Code")).findFirst().orElseThrow();
        assertFalse(cleanCode.isBorrowed());
        assertEquals("Robert C. Martin", cleanCode.getAuthor());
        assertEquals(Genre.PROGRAMMING, cleanCode.getGenre());

        Book dune = loaded.stream().filter(b -> b.getTitle().equals("Dune")).findFirst().orElseThrow();
        assertTrue(dune.isBorrowed());
        assertEquals(LocalDate.now().plusDays(14), dune.getDueDate());
    }

    @Test
    void loadOnEmptyFileReturnsAnEmptyListInsteadOfCrashing() throws IOException {
        new File(TEST_FILE).createNewFile();
        assertTrue(LibraryStorage.load(TEST_FILE).isEmpty());
    }

    @Test
    void loadOnCorruptedFileReturnsAnEmptyListInsteadOfCrashing() throws IOException {
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("{ this is not valid JSON ]");
        }
        assertTrue(LibraryStorage.load(TEST_FILE).isEmpty());
    }

    @Test
    void loadSkipsEntriesWithAnUnknownGenreInsteadOfFailing() throws IOException {
        Files.writeString(new File(TEST_FILE).toPath(),
                "[{\"title\":\"Mystery Book\",\"author\":\"Someone\",\"genre\":\"NOT_A_REAL_GENRE\",\"dueDate\":null}]");

        List<Book> loaded = LibraryStorage.load(TEST_FILE);
        assertEquals(1, loaded.size());
        assertEquals(Genre.OTHER, loaded.get(0).getGenre());
    }
}
