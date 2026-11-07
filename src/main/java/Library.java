import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Library {
    private ArrayList<Book> books = new ArrayList<>();

    public boolean addBook(Book book) {
        if (findByTitle(book.getTitle()) != null) {
            System.out.println("\"" + book.getTitle() + "\" is already in the library.");
            return false;
        }
        books.add(book);
        return true;
    }

    public boolean removeBook(String title) {
        Book book = findByTitle(title);
        if (book == null) {
            System.out.println("Sorry, we don't have \"" + title + "\".");
            return false;
        }
        books.remove(book);
        System.out.println("Removed \"" + book.getTitle() + "\".");
        return true;
    }

    public Book findByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    public List<Book> searchByKeyword(String keyword) {
        String needle = keyword.toLowerCase();
        List<Book> matches = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(needle) || book.getAuthor().toLowerCase().contains(needle)) {
                matches.add(book);
            }
        }
        return matches;
    }

    public boolean borrowBook(String title) {
        Book book = findByTitle(title);
        if (book == null) {
            System.out.println("Sorry, we don't have \"" + title + "\".");
            return false;
        } else if (book.isBorrowed()) {
            System.out.println("\"" + book.getTitle() + "\" is already borrowed.");
            return false;
        }
        book.markBorrowed();
        System.out.println("You borrowed \"" + book.getTitle() + "\". Due back " + book.getDueDate() + ".");
        return true;
    }

    public boolean returnBook(String title) {
        Book book = findByTitle(title);
        if (book == null) {
            System.out.println("Sorry, we don't have \"" + title + "\".");
            return false;
        } else if (!book.isBorrowed()) {
            System.out.println("\"" + book.getTitle() + "\" was not borrowed.");
            return false;
        }
        book.markReturned();
        System.out.println("You returned \"" + book.getTitle() + "\".");
        return true;
    }

    public boolean editBook(String title, String newAuthor, Genre newGenre) {
        Book book = findByTitle(title);
        if (book == null) {
            System.out.println("Sorry, we don't have \"" + title + "\".");
            return false;
        }
        if (newAuthor != null && !newAuthor.isBlank()) {
            book.setAuthor(newAuthor);
        }
        if (newGenre != null) {
            book.setGenre(newGenre);
        }
        System.out.println("Updated \"" + book.getTitle() + "\".");
        return true;
    }

    public List<Book> getAllBooks() {
        return Collections.unmodifiableList(books);
    }

    public void setAllBooks(List<Book> loadedBooks) {
        books = new ArrayList<>(loadedBooks);
    }

    public void listAvailable() {
        System.out.println("Available books:");
        boolean found = false;
        for (Book book : books) {
            if (!book.isBorrowed()) {
                System.out.println(" - " + book);
                found = true;
            }
        }
        if (!found) {
            System.out.println(" (none available)");
        }
    }

    public void listAll() {
        System.out.println("All books:");
        if (books.isEmpty()) {
            System.out.println(" (no books yet)");
            return;
        }
        for (Book book : books) {
            System.out.println(" - " + book);
        }
    }

    public void listByGenre(Genre genre) {
        System.out.println("Books in " + genre + ":");
        boolean found = false;
        for (Book book : books) {
            if (book.getGenre() == genre) {
                System.out.println(" - " + book);
                found = true;
            }
        }
        if (!found) {
            System.out.println(" (none)");
        }
    }

    public void listOverdue() {
        System.out.println("Overdue books:");
        boolean found = false;
        for (Book book : books) {
            if (book.isOverdue()) {
                System.out.println(" - " + book);
                found = true;
            }
        }
        if (!found) {
            System.out.println(" (nothing overdue)");
        }
    }
}
