public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        library.addBook(new Book("Clean Code", "Robert C. Martin"));
        library.addBook(new Book("Effective Java", "Joshua Bloch"));
        library.addBook(new Book("Head First Java", "Kathy Sierra"));

        library.listAvailable();
        library.borrowBook("clean code");
        library.borrowBook("Clean Code");
        library.borrowBook("Missing Book");
        library.listAvailable();
        library.returnBook("Clean Code");
        library.listAvailable();
    }
}