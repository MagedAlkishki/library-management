import java.util.ArrayList;

public class Library {
    private ArrayList<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
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
}