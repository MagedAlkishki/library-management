import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class Library {
    private ArrayList<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public Book findByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    public void borrowBook(String title) {
        Book book = findByTitle(title);
        if (book == null) {
            System.out.println("Sorry, we don't have \"" + title + "\".");
        } else if (book.isBorrowed()) {
            System.out.println("\"" + book.getTitle() + "\" is already borrowed.");
        } else {
            book.setBorrowed(true);
            System.out.println("You borrowed \"" + book.getTitle() + "\".");
        }
    }

    public void returnBook(String title) {
        Book book = findByTitle(title);
        if (book == null) {
            System.out.println("Sorry, we don't have \"" + title + "\".");
        } else if (!book.isBorrowed()) {
            System.out.println("\"" + book.getTitle() + "\" was not borrowed.");
        } else {
            book.setBorrowed(false);
            System.out.println("You returned \"" + book.getTitle() + "\".");
        }
    }

    public void listAvailable() {
        System.out.println("Available books:");
        for (Book book : books) {
            if (!book.isBorrowed()) {
                System.out.println(" - " + book);
            }
        }
    }
}

