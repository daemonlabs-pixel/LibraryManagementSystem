package models;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import enums.LoanStatus;

public class Loan {

    private final int id;
    private final Member member;
    private final Book book;

    private final LocalDate borrowedDate;
    private final LocalDate dueDate;

    private LocalDate returnDate;
    private LoanStatus status;

    private static final long DAILY_LATE_FEE = 200;

    public Loan(int id, Member member, Book book,
                String borrowedDateString, String dueDateString,
                String returnDateString, LoanStatus status) {

        if (id <= 0 || member == null || book == null || status == null) {
            throw new IllegalArgumentException("Invalid loan information.");
        }

        if (borrowedDateString == null || borrowedDateString.isBlank()
                || dueDateString == null || dueDateString.isBlank()) {
            throw new IllegalArgumentException("Borrowed date and due date are required.");
        }

        LocalDate borrowed;
        LocalDate due;
        LocalDate returned = null;

        try {
            borrowed = LocalDate.parse(borrowedDateString);
            due = LocalDate.parse(dueDateString);

            if (returnDateString != null) {
                if (returnDateString.isBlank()) {
                    throw new IllegalArgumentException("Invalid return date.");
                }

                returned = LocalDate.parse(returnDateString);
            }

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format.", e);
        }

        if (!borrowed.isBefore(due)) {
            throw new IllegalArgumentException(
                "The borrowed date must be before the due date."
            );
        }

        if (returned != null && returned.isBefore(borrowed)) {
            throw new IllegalArgumentException(
                "The return date cannot be before the borrowed date."
            );
        }

        if ((status == LoanStatus.RETURNED && returned == null)
                || (status != LoanStatus.RETURNED && returned != null)) {
            throw new IllegalArgumentException(
                "Loan status and return date are inconsistent."
            );
        }

        this.id = id;
        this.member = member;
        this.book = book;
        this.borrowedDate = borrowed;
        this.dueDate = due;
        this.returnDate = returned;
        this.status = status;
    }

    // Getters

    public int getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    // Loan management

    public boolean isOverdue() {

        if (status == LoanStatus.RETURNED) {
            return returnDate.isAfter(dueDate);
        }

        return LocalDate.now().isAfter(dueDate);
    }

    public boolean returnBook(String returnDateString) {

        if (status == LoanStatus.RETURNED) {
            return false;
        }

        if (returnDateString == null || returnDateString.isBlank()) {
            throw new IllegalArgumentException("Return date is required.");
        }

        LocalDate date;

        try {
            date = LocalDate.parse(returnDateString);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid return date.", e);
        }

        if (date.isBefore(borrowedDate)) {
            throw new IllegalArgumentException(
                "Return date cannot be before the borrowed date."
            );
        }

        this.returnDate = date;
        this.status = LoanStatus.RETURNED;

        return true;
    }

    public boolean returnBook() {
        return returnBook(LocalDate.now().toString());
    }

    // Late fee calculation

    public long calculateLateFee() {

        if (status != LoanStatus.RETURNED || returnDate == null
                || !returnDate.isAfter(dueDate)) {
            return 0;
        }

        long days = ChronoUnit.DAYS.between(dueDate, returnDate);

        return days * DAILY_LATE_FEE;
    }

    @Override
    public String toString() {

        return "ID: " + id
                + " | Member: " + member.getUsername()
                + " | Book: " + book.getTitle()
                + " | Borrowed date: " + borrowedDate
                + " | Due date: " + dueDate
                + " | Return date: " + returnDate
                + " | Status: " + status;
    }
}