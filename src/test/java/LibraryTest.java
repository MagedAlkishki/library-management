import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    @Test
    void addBookAddsItToTheLibrary() {
        Library library = new Library();
        assertTrue(library.addBook(new Book("Dune", "Frank Herbert", Genre.FICTION)));
        assertEquals(1, library.getAllBooks().size());
    }

    @Test
    void addBookRejectsDuplicateTitlesCaseInsensitively() {
        Library library = new Library();
        library.addBook(new Book("Dune", "Frank Herbert", Genre.FICTION));
        assertFalse(library.addBook(new Book("dune", "Someone Else", Genre.FICTION)));
        assertEquals(1, library.getAllBooks().size());
    }

    @Test
    void findByTitleIsCaseInsensitive() {
        Library library = new Library();
        library.addBook(new Book("Clean Code", "Robert C. Martin", Genre.PROGRAMMING));
        assertNotNull(library.findByTitle("clean code"));
        assertNull(library.findByTitle("Missing Book"));
    }

    @Test
    void borrowBookMarksItBorrowedWithADueDate() {
        Library library = new Library();
        library.addBook(new Book("Dune", "Frank Herbert", Genre.FICTION));

        assertTrue(library.borrowBook("Dune"));
        Book book = library.findByTitle("Dune");
        assertTrue(book.isBorrowed());
        assertNotNull(book.getDueDate());
        assertFalse(book.isOverdue());
    }

    @Test
    void cannotBorrowABookThatIsAlreadyBorrowed() {
        Library library = new Library();
        library.addBook(new Book("Dune", "Frank Herbert", Genre.FICTION));
        library.borrowBook("Dune");

        assertFalse(library.borrowBook("Dune"));
    }

    @Test
    void cannotBorrowOrReturnABookThatDoesNotExist() {
        Library library = new Library();
        assertFalse(library.borrowBook("Nonexistent"));
        assertFalse(library.returnBook("Nonexistent"));
    }

    @Test
    void returnBookClearsBorrowedStateAndDueDate() {
        Library library = new Library();
        library.addBook(new Book("Dune", "Frank Herbert", Genre.FICTION));
        library.borrowBook("Dune");

        assertTrue(library.returnBook("Dune"));
        Book book = library.findByTitle("Dune");
        assertFalse(book.isBorrowed());
        assertNull(book.getDueDate());
    }

    @Test
    void cannotReturnABookThatWasNotBorrowed() {
        Library library = new Library();
        library.addBook(new Book("Dune", "Frank Herbert", Genre.FICTION));
        assertFalse(library.returnBook("Dune"));
    }

    @Test
    void listOverdueOnlyReportsBooksPastTheirDueDate() {
        Library library = new Library();
        Book onTime = new Book("Dune", "Frank Herbert", Genre.FICTION);
        onTime.restoreBorrowedState(LocalDate.now().plusDays(5));
        Book late = new Book("Effective Java", "Joshua Bloch", Genre.PROGRAMMING);
        late.restoreBorrowedState(LocalDate.now().minusDays(2));

        library.addBook(onTime);
        library.addBook(late);

        assertFalse(onTime.isOverdue());
        assertTrue(late.isOverdue());
    }

    @Test
    void searchByKeywordMatchesTitleOrAuthorCaseInsensitively() {
        Library library = new Library();
        library.addBook(new Book("Clean Code", "Robert C. Martin", Genre.PROGRAMMING));
        library.addBook(new Book("Dune", "Frank Herbert", Genre.FICTION));

        List<Book> byTitle = library.searchByKeyword("clean");
        assertEquals(1, byTitle.size());
        assertEquals("Clean Code", byTitle.get(0).getTitle());

        List<Book> byAuthor = library.searchByKeyword("herbert");
        assertEquals(1, byAuthor.size());
        assertEquals("Dune", byAuthor.get(0).getTitle());

        assertTrue(library.searchByKeyword("nonexistent").isEmpty());
    }

    @Test
    void editBookUpdatesAuthorAndGenreButLeavesTitleAlone() {
        Library library = new Library();
        library.addBook(new Book("Dune", "Frank Herbert", Genre.FICTION));

        assertTrue(library.editBook("Dune", "F. Herbert", Genre.SCIENCE));
        Book book = library.findByTitle("Dune");
        assertEquals("F. Herbert", book.getAuthor());
        assertEquals(Genre.SCIENCE, book.getGenre());
        assertEquals("Dune", book.getTitle());
    }

    @Test
    void editBookWithNullsLeavesExistingValuesUnchanged() {
        Library library = new Library();
        library.addBook(new Book("Dune", "Frank Herbert", Genre.FICTION));

        assertTrue(library.editBook("Dune", null, null));
        Book book = library.findByTitle("Dune");
        assertEquals("Frank Herbert", book.getAuthor());
        assertEquals(Genre.FICTION, book.getGenre());
    }

    @Test
    void removeBookTakesItOutOfTheLibrary() {
        Library library = new Library();
        library.addBook(new Book("Dune", "Frank Herbert", Genre.FICTION));

        assertTrue(library.removeBook("Dune"));
        assertNull(library.findByTitle("Dune"));
    }

    @Test
    void removeBookFailsWhenTitleIsNotFound() {
        Library library = new Library();
        assertFalse(library.removeBook("Nonexistent"));
    }
}
