package models;

public class Book {

    private final int id;
    private final String title;
    private final String author;

    private int total_copies;
    private int available_copies;

    public Book(int id, String title, String author,
                int total_copies, int available_copies) {

        if (id <= 0 || title == null || title.isBlank()
                || author == null || author.isBlank()
                || total_copies <= 0
                || available_copies < 0
                || available_copies > total_copies) {

            throw new IllegalArgumentException(
                "Invalid information regarding book creation."
            );
        }

        this.id = id;
        this.title = title;
        this.author = author;
        this.total_copies = total_copies;
        this.available_copies = available_copies;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getTotal_copies() {
        return total_copies;
    }

    public int getAvailable_copies() {
        return available_copies;
    }

    // Copy management

    public void setTotal_copies(int total_copies) {

        if (total_copies <= 0 || total_copies < available_copies) {
            throw new IllegalArgumentException(
                "Total copies cannot be less than available copies."
            );
        }

        this.total_copies = total_copies;
    }

    public void setAvailable_copies(int available_copies) {

        if (available_copies < 0 || available_copies > total_copies) {
            throw new IllegalArgumentException(
                "Invalid value for the number of available copies."
            );
        }

        this.available_copies = available_copies;
    }

    public void updateCopies(int total_copies, int available_copies) {

        if (total_copies <= 0 || available_copies < 0
                || available_copies > total_copies) {

            throw new IllegalArgumentException(
                "Invalid number of copies."
            );
        }

        this.total_copies = total_copies;
        this.available_copies = available_copies;
    }

    public boolean borrowCopy() {

        if (available_copies == 0) {
            return false;
        }

        available_copies--;
        return true;
    }

    public boolean returnCopy() {

        if (available_copies >= total_copies) {
            return false;
        }

        available_copies++;
        return true;
    }

    @Override
    public String toString() {

        return "ID: " + id
                + " | Title: " + title
                + " | Author: " + author
                + " | Total copies: " + total_copies
                + " | Available copies: " + available_copies;
    }
}
