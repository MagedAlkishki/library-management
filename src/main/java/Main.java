import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DATA_FILE = "library-data.json";

    public static void main(String[] args) {
        Library library = new Library();
        List<Book> loaded = LibraryStorage.load(DATA_FILE);

        if (loaded.isEmpty()) {
            // First run, or the save file was empty/missing: seed a few books
            // so the menu isn't staring at an empty library.
            library.addBook(new Book("Clean Code", "Robert C. Martin", Genre.PROGRAMMING));
            library.addBook(new Book("Effective Java", "Joshua Bloch", Genre.PROGRAMMING));
            library.addBook(new Book("Dune", "Frank Herbert", Genre.FICTION));
            LibraryStorage.save(library.getAllBooks(), DATA_FILE);
        } else {
            library.setAllBooks(loaded);
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    if (addBookFromInput(library, scanner)) {
                        LibraryStorage.save(library.getAllBooks(), DATA_FILE);
                    }
                }
                case "2" -> {
                    if (library.borrowBook(readRequiredLine(scanner, "Title to borrow: "))) {
                        LibraryStorage.save(library.getAllBooks(), DATA_FILE);
                    }
                }
                case "3" -> {
                    if (library.returnBook(readRequiredLine(scanner, "Title to return: "))) {
                        LibraryStorage.save(library.getAllBooks(), DATA_FILE);
                    }
                }
                case "4" -> library.listAvailable();
                case "5" -> library.listAll();
                case "6" -> library.listByGenre(chooseGenre(scanner));
                case "7" -> {
                    if (library.removeBook(readRequiredLine(scanner, "Title to remove: "))) {
                        LibraryStorage.save(library.getAllBooks(), DATA_FILE);
                    }
                }
                case "8" -> searchOpenLibrary(library, scanner);
                case "9" -> searchLocalLibrary(library, scanner);
                case "10" -> library.listOverdue();
                case "11" -> {
                    if (editBookFromInput(library, scanner)) {
                        LibraryStorage.save(library.getAllBooks(), DATA_FILE);
                    }
                }
                case "0" -> {
                    LibraryStorage.save(library.getAllBooks(), DATA_FILE);
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Please choose a number from the menu.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("=== Library Menu ===");
        System.out.println("1. Add a book");
        System.out.println("2. Borrow a book");
        System.out.println("3. Return a book");
        System.out.println("4. List available books");
        System.out.println("5. List all books");
        System.out.println("6. List books by genre");
        System.out.println("7. Remove a book");
        System.out.println("8. Search Open Library for a book");
        System.out.println("9. Search your library (title/author)");
        System.out.println("10. List overdue books");
        System.out.println("11. Edit a book's author/genre");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    // Prints the prompt and keeps re-prompting instead of silently treating
    // a blank line as a valid (empty) title, since an empty title can never
    // match a book.
    private static String readRequiredLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        while (line.isEmpty()) {
            System.out.print("That can't be empty. " + prompt);
            line = scanner.nextLine().trim();
        }
        return line;
    }

    private static boolean addBookFromInput(Library library, Scanner scanner) {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("The title can't be empty.");
            return false;
        }
        if (library.findByTitle(title) != null) {
            System.out.println("\"" + title + "\" is already in the library.");
            return false;
        }

        System.out.print("Author: ");
        String author = scanner.nextLine().trim();
        if (author.isEmpty()) {
            author = "Unknown author";
        }

        Genre genre = chooseGenre(scanner);

        System.out.println();
        System.out.println("About to add:");
        System.out.println("  Title:  " + title);
        System.out.println("  Author: " + author);
        System.out.println("  Genre:  " + genre);
        System.out.print("Add this book? (y/n): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("y")) {
            library.addBook(new Book(title, author, genre));
            System.out.println("Added: " + title);
            return true;
        }
        System.out.println("Cancelled, nothing was added.");
        return false;
    }

    private static boolean editBookFromInput(Library library, Scanner scanner) {
        String title = readRequiredLine(scanner, "Title to edit: ");
        if (library.findByTitle(title) == null) {
            System.out.println("Sorry, we don't have \"" + title + "\".");
            return false;
        }

        System.out.print("New author (leave blank to keep it): ");
        String author = scanner.nextLine().trim();

        System.out.print("Change genre? (y/n): ");
        Genre genre = null;
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            genre = chooseGenre(scanner);
        }

        return library.editBook(title, author.isEmpty() ? null : author, genre);
    }

    private static Genre chooseGenre(Scanner scanner) {
        Genre[] genres = Genre.values();
        System.out.println("Genres:");
        for (int i = 0; i < genres.length; i++) {
            System.out.println("  " + (i + 1) + ". " + genres[i]);
        }
        System.out.print("Choose a genre number: ");
        try {
            int number = Integer.parseInt(scanner.nextLine().trim());
            if (number >= 1 && number <= genres.length) {
                return genres[number - 1];
            }
            System.out.println("Unknown number, using OTHER.");
        } catch (NumberFormatException e) {
            System.out.println("That is not a number, using OTHER.");
        }
        return Genre.OTHER;
    }

    private static void searchLocalLibrary(Library library, Scanner scanner) {
        String keyword = readRequiredLine(scanner, "Search your library for: ");
        List<Book> matches = library.searchByKeyword(keyword);
        if (matches.isEmpty()) {
            System.out.println("No books in your library match \"" + keyword + "\".");
            return;
        }
        System.out.println("Matches:");
        for (Book book : matches) {
            System.out.println(" - " + book);
        }
    }

    private static void searchOpenLibrary(Library library, Scanner scanner) {
        System.out.print("Search for a book title: ");
        String query = scanner.nextLine().trim();
        if (query.isEmpty()) { System.out.println("Type something to search for."); return; }

        System.out.println("Searching Open Library...");
        List<BookSearch.Result> results = BookSearch.search(query);

        if (results.isEmpty()) {
            System.out.println("No results found.");
            return;
        }

        for (int i = 0; i < results.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + results.get(i));
        }

        System.out.print("Add one to your library? Enter its number, or 0 to skip: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= results.size()) {
                BookSearch.Result picked = results.get(choice - 1);
                if (library.findByTitle(picked.getTitle()) != null) {
                    System.out.println("\"" + picked.getTitle() + "\" is already in your library.");
                    return;
                }
                Genre genre = chooseGenre(scanner);
                library.addBook(new Book(picked.getTitle(), picked.getAuthor(), genre));
                LibraryStorage.save(library.getAllBooks(), DATA_FILE);
                System.out.println("Added \"" + picked.getTitle() + "\" to your library.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Not a number, skipping.");
        }
    }
}
