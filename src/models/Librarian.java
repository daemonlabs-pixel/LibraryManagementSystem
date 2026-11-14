package models;

import java.util.List;
import services.Library;

public class Librarian extends Account {

    private final Library library;

    public Librarian(int id, String username, String password, Library library) {

        super(id, username, password);

        if (library == null) {
            throw new IllegalArgumentException("Library cannot be null.");
        }

        this.library = library;
    }

    @Override
    public String getRole() {
        return "Librarian";
    }

    public void addBook(Book book) {
        library.addBook(book);
    }

    public boolean removeBook(int bookId) {
        return library.removeBook(bookId);
    }

    public boolean updateBook(int bookId, int totalCopies, int availableCopies) {

        Book book = library.findBook(bookId);

        if (book == null) {
            return false;
        }

        book.updateCopies(totalCopies, availableCopies);

        return true;
    }

    public boolean displayBooks() {

        List<Book> books = library.getBooks();

        if (books.isEmpty()) {
            return false;
        }

        for (Book book : books) {
            System.out.println(book);
        }

        return true;
    }
}