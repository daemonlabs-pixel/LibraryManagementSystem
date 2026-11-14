package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import enums.LoanStatus;
import interfaces.Borrowable;
import models.Book;
import models.Loan;
import models.Member;

public class LibraryManager implements Borrowable {

    private final Library library;

    private static final int MAX_ACTIVE_LOANS = 3;
    private static final int LOAN_DURATION_DAYS = 14;

    public LibraryManager(Library library) {

        if (library == null) {
            throw new IllegalArgumentException("Library cannot be null.");
        }

        this.library = library;
    }

    @Override
    public boolean borrowBook(int memberId, int bookId) {

        if (memberId <= 0 || bookId <= 0) {
            return false;
        }

        Book book = library.findBook(bookId);
        Member member = library.findMember(memberId);

        // 1. Verify that both objects exist.
        if (member == null || book == null) {
            return false;
        }

        // 2. Verify the member's library card.
        if (member.getLibraryCard() == null
                || !member.getLibraryCard().isValid()) {
            return false;
        }

        // 3. Verify the borrowing limit.
        if (member.getActiveLoans().size() >= MAX_ACTIVE_LOANS) {
            return false;
        }

        // 4. Verify book availability and deduct one copy.
        if (!book.borrowCopy()) {
            return false;
        }

        boolean addedToLibrary = false;
        Loan loan = null;

        try {

            // 5. Create the loan.
            LocalDate borrowedDate = LocalDate.now();
            LocalDate dueDate = borrowedDate.plusDays(LOAN_DURATION_DAYS);

            loan = new Loan(
                library.generateLoanId(),
                member,
                book,
                borrowedDate.toString(),
                dueDate.toString(),
                null,
                LoanStatus.ACTIVE
            );

            // 6. Register the loan in the library.
            library.addLoan(loan);
            addedToLibrary = true;

            // 7. Register the loan in the member's history.
            member.addLoan(loan);

        } catch (RuntimeException e) {

            // 8. Rollback successful registrations.
            if (addedToLibrary) {
                library.removeLoan(loan.getId());
            }

            // 9. Restore the book's available copies.
            book.returnCopy();

            throw e;
        }

        // 10. Borrowing succeeded.
        return true;
    }

    @Override
    public boolean returnBook(int memberId, int bookId) {

        if (memberId <= 0 || bookId <= 0) {
            return false;
        }

        Book book = library.findBook(bookId);
        Member member = library.findMember(memberId);

        if (member == null || book == null) {
            return false;
        }

        Loan loanToReturn = null;

        List<Loan> activeLoans = member.getActiveLoans();

        // Find the active loan corresponding to this book.
        for (Loan loan : activeLoans) {

            if (loan.getBook().getId() == bookId) {
                loanToReturn = loan;
                break;
            }
        }

        if (loanToReturn == null) {
            return false;
        }

        // Make sure the book has room for another available copy.
        if (book.getAvailable_copies() >= book.getTotal_copies()) {
            return false;
        }

        // Register the return date and update the loan status.
        if (!loanToReturn.returnBook()) {
            return false;
        }

        // Restore the available copy.
        if (!book.returnCopy()) {
            return false;
        }

        return true;
    }

    public List<Loan> getOverdueLoans() {

        List<Loan> overdueLoans = new ArrayList<>();

        for (Loan loan : library.getLoans()) {

            if (loan.isOverdue()) {
                overdueLoans.add(loan);
            }
        }

        return Collections.unmodifiableList(overdueLoans);
    }

    public long getTotalLateFees() {

        long totalLateFees = 0;

        for (Loan loan : getOverdueLoans()) {
            totalLateFees += loan.calculateLateFee();
        }

        return totalLateFees;
    }

    public List<Loan> getMemberLoanHistory(int memberId) {

        if (memberId <= 0) {
            throw new IllegalArgumentException("Invalid member ID.");
        }

        Member member = library.findMember(memberId);

        if (member == null) {
            throw new IllegalArgumentException("Member not found.");
        }

        return member.getLoans();
    }
}