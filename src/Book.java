public class Book {
    private String Title;
    private String Author;
    private boolean Borrowed;

    public Book(String title, String author){
        this.Title = title;
        this.Author = author;
        this.Borrowed = false;
    }

    public String getTitle(){
        return Title;
    }

    public String getAuthor(){
        return Author;
    }

    public boolean isBorrowed(){
        return Borrowed;
    }

    public void setBorrowed(boolean borrowed){
        this.Borrowed = borrowed;
    }

    public String toString(){
        return Title + " by "  + Author +  (Borrowed ? " (borrowed)" : "(available)");
    }
}
