import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        library.addBook(new Book("Clean Code", "Robert C. Martin", Genre.PROGRAMMING));
        library.addBook(new Book("Effective Java", "Joshua Bloch", Genre.PROGRAMMING));
        library.addBook(new Book("Dune", "Frank Herbert", Genre.FICTION));

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addBookFromInput(library, scanner);
                case "2" -> {
                    System.out.print("Title to borrow: ");
                    library.borrowBook(scanner.nextLine().trim());
                }
                case "3" -> {
                    System.out.print("Title to return: ");
                    library.returnBook(scanner.nextLine().trim());
                }
                case "4" -> library.listAvailable();
                case "5" -> library.listAll();
                case "6" -> library.listByGenre(chooseGenre(scanner));
                case "7" -> {
                    System.out.print("Title to remove: ");
                    library.removeBook(scanner.nextLine().trim());
                }
                case "8" -> searchOpenLibrary(library, scanner);
                case "0" -> {
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
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private static void addBookFromInput(Library library, Scanner scanner) {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("The title can't be empty.");
            return;
        }
        System.out.print("Author: ");
        String author = scanner.nextLine().trim();

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
        } else {
            System.out.println("Cancelled, nothing was added.");
        }
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
                Genre genre = chooseGenre(scanner);
                library.addBook(new Book(picked.getTitle(), picked.getAuthor(), genre));
                System.out.println("Added \"" + picked.getTitle() + "\" to your library.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Not a number, skipping.");
        }
    }
}