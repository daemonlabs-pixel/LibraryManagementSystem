package interfaces;

public interface Borrowable {

    boolean borrowBook(int memberId, int bookId);

    boolean returnBook(int memberId, int bookId);

}