import java.time.LocalDate;

public class Book {
    private static final int LOAN_PERIOD_DAYS = 14;

    private String title;
    private String author;
    private Genre genre;
    private boolean borrowed;
    private LocalDate dueDate;

    public Book(String title, String author, Genre genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.borrowed = false;
        this.dueDate = null;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isOverdue() {
        return borrowed && dueDate != null && dueDate.isBefore(LocalDate.now());
    }

    public void markBorrowed() {
        borrowed = true;
        dueDate = LocalDate.now().plusDays(LOAN_PERIOD_DAYS);
    }

    public void markReturned() {
        borrowed = false;
        dueDate = null;
    }

    // Used when restoring a book from the saved library file, where the due
    // date already exists and shouldn't be recalculated from today.
    public void restoreBorrowedState(LocalDate dueDate) {
        this.borrowed = dueDate != null;
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        if (!borrowed) {
            return title + " by " + author + " [" + genre + "] (available)";
        }
        String status = isOverdue() ? "OVERDUE, was due " + dueDate : "due " + dueDate;
        return title + " by " + author + " [" + genre + "] (borrowed, " + status + ")";
    }
}
