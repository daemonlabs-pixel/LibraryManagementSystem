import java.util.Scanner;
import java.util.List;

import models.*;
import services.Library;
import services.LibraryManager;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static Library library;
    private static LibraryManager libraryManager;

    private static Administrator administrator;
    private static Librarian librarian;

    public static void main(String[] args) {

        initializeLibrary();

        boolean running = true;

        while (running) {

            System.out.println("\n========== LIBRARY MANAGEMENT SYSTEM ==========");
            System.out.println("1. Administrator login");
            System.out.println("2. Librarian login");
            System.out.println("3. Member login");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = readInt();

            switch (choice) {

                case 1:
                    administratorLogin();
                    break;

                case 2:
                    librarianLogin();
                    break;

                case 3:
                    memberLogin();
                    break;

                case 0:
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }

    // ==========================================
    // INITIALIZATION
    // ==========================================

    private static void initializeLibrary() {

        library = new Library(1, "IFRI Library");
        libraryManager = new LibraryManager(library);

        administrator = new Administrator(
            1, "admin", "admin123"
        );

        librarian = new Librarian(
            2, "librarian", "lib123", library
        );

        // Create some sample books.
        library.addBook(new Book(1, "Clean Code", "Robert C. Martin", 5, 5));
        library.addBook(new Book(2, "Effective Java", "Joshua Bloch", 3, 3));
        library.addBook(new Book(3, "Introduction to Algorithms",
                "Thomas H. Cormen", 4, 4));

        // Register the librarian in the administrator's account list.
        administrator.addAccount(librarian);

        System.out.println("Library initialized successfully.");
    }

    // ==========================================
    // AUTHENTICATION
    // ==========================================

    private static void administratorLogin() {

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (!administrator.getUsername().equals(username)
                || !administrator.authenticate(password)) {

            System.out.println("Invalid credentials.");
            return;
        }

        System.out.println("\nWelcome, Administrator!");

        administratorMenu();
    }

    private static void librarianLogin() {

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (!librarian.getUsername().equals(username)
                || !librarian.authenticate(password)) {

            System.out.println("Invalid credentials.");
            return;
        }

        System.out.println("\nWelcome, Librarian!");

        librarianMenu();
    }

    private static void memberLogin() {

        System.out.print("Member ID: ");
        int memberId = readInt();

        Member member = library.findMember(memberId);

        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (!member.authenticate(password)) {
            System.out.println("Invalid credentials.");
            return;
        }

        System.out.println("\nWelcome, " + member.getUsername() + "!");

        memberMenu(member);
    }

    // ==========================================
    // ADMINISTRATOR MENU
    // ==========================================

    private static void administratorMenu() {

        boolean running = true;

        while (running) {

            System.out.println("\n========== ADMINISTRATOR MENU ==========");
            System.out.println("1. Display accounts");
            System.out.println("2. Display library information");
            System.out.println("0. Logout");
            System.out.print("Choose an option: ");

            int choice = readInt();

            switch (choice) {

                case 1:
                    if (!administrator.displayAccounts()) {
                        System.out.println("No accounts registered.");
                    }
                    break;

                case 2:
                    System.out.println(library);
                    break;

                case 0:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ==========================================
    // LIBRARIAN MENU
    // ==========================================

    private static void librarianMenu() {

        boolean running = true;

        while (running) {

            System.out.println("\n========== LIBRARIAN MENU ==========");
            System.out.println("1. Display books");
            System.out.println("2. Add a book");
            System.out.println("3. Remove a book");
            System.out.println("4. Update book copies");
            System.out.println("0. Logout");
            System.out.print("Choose an option: ");

            int choice = readInt();

            switch (choice) {

                case 1:
                    if (!librarian.displayBooks()) {
                        System.out.println("No books available.");
                    }
                    break;

                case 2:
                    addBookMenu();
                    break;

                case 3:
                    removeBookMenu();
                    break;

                case 4:
                    updateBookMenu();
                    break;

                case 0:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void addBookMenu() {

        try {

            System.out.print("Book ID: ");
            int id = readInt();

            System.out.print("Title: ");
            String title = scanner.nextLine();

            System.out.print("Author: ");
            String author = scanner.nextLine();

            System.out.print("Total copies: ");
            int totalCopies = readInt();

            System.out.print("Available copies: ");
            int availableCopies = readInt();

            Book book = new Book(
                id, title, author, totalCopies, availableCopies
            );

            librarian.addBook(book);

            System.out.println("Book added successfully.");

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void removeBookMenu() {

        System.out.print("Enter book ID: ");
        int bookId = readInt();

        if (librarian.removeBook(bookId)) {
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Book not found or cannot be removed.");
        }
    }

    private static void updateBookMenu() {

        System.out.print("Enter book ID: ");
        int bookId = readInt();

        System.out.print("New total copies: ");
        int totalCopies = readInt();

        System.out.print("New available copies: ");
        int availableCopies = readInt();

        try {

            if (librarian.updateBook(bookId, totalCopies, availableCopies)) {
                System.out.println("Book updated successfully.");
            } else {
                System.out.println("Book not found.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ==========================================
    // MEMBER MENU
    // ==========================================

    private static void memberMenu(Member member) {

        boolean running = true;

        while (running) {

            System.out.println("\n========== MEMBER MENU ==========");
            System.out.println("1. Display books");
            System.out.println("2. Borrow a book");
            System.out.println("3. Return a book");
            System.out.println("4. Display my active loans");
            System.out.println("5. Display my loan history");
            System.out.println("6. Display my late fees");
            System.out.println("0. Logout");
            System.out.print("Choose an option: ");

            int choice = readInt();

            switch (choice) {

                case 1:
                    librarian.displayBooks();
                    break;

                case 2:
                    System.out.print("Enter book ID: ");
                    int borrowBookId = readInt();

                    if (libraryManager.borrowBook(
                            member.getId(), borrowBookId)) {

                        System.out.println("Book borrowed successfully.");

                    } else {
                        System.out.println(
                            "Borrowing failed. Check your card, "
                            + "borrowing limit and book availability."
                        );
                    }
                    break;

                case 3:
                    System.out.print("Enter book ID: ");
                    int returnBookId = readInt();

                    if (libraryManager.returnBook(
                            member.getId(), returnBookId)) {

                        System.out.println("Book returned successfully.");

                    } else {
                        System.out.println("Return failed.");
                    }
                    break;

                case 4:
                    displayLoans(member.getActiveLoans());
                    break;

                case 5:
                    displayLoans(member.getLoans());
                    break;

                case 6:
                    displayMemberLateFees(member);
                    break;

                case 0:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ==========================================
    // LOAN MANAGEMENT
    // ==========================================

    private static void displayLoans(List<Loan> loans) {

        if (loans.isEmpty()) {
            System.out.println("No loans found.");
            return;
        }

        for (Loan loan : loans) {
            System.out.println(loan);
        }
    }

    private static void displayMemberLateFees(Member member) {

        long total = 0;

        for (Loan loan : member.getLoans()) {
            total += loan.calculateLateFee();
        }

        System.out.println("Total late fees: " + total);
    }

    // ==========================================
    // INPUT HANDLING
    // ==========================================

    private static int readInt() {

        while (true) {

            String input = scanner.nextLine();

            try {
                return Integer.parseInt(input.trim());

            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
