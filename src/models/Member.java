package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import enums.LoanStatus;

public class Member extends Account {

    private LibraryCard libraryCard;
    private final List<Loan> loans;

    public Member(int id, String username, String password) {
        super(id, username, password);

        this.loans = new ArrayList<>();
    }

    // Getters

    public LibraryCard getLibraryCard() {
        return libraryCard;
    }

    public List<Loan> getLoans() {
        return Collections.unmodifiableList(loans);
    }

    // Loan management

    public void addLoan(Loan loan) {

        if (loan == null || loan.getMember() != this) {
            throw new IllegalArgumentException("Invalid loan.");
        }

        for (Loan existingLoan : loans) {
            if (existingLoan.getId() == loan.getId()) {
                throw new IllegalArgumentException("Loan already exists.");
            }
        }

        loans.add(loan);
    }

    public boolean removeLoan(int loanId) {

        if (loanId <= 0) {
            return false;
        }

        for (int i = 0; i < loans.size(); i++) {

            Loan loan = loans.get(i);

            if (loan.getId() == loanId) {
                loans.remove(i);
                return true;
            }
        }

        return false;
    }

    public List<Loan> getActiveLoans() {

        List<Loan> activeLoans = new ArrayList<>();

        for (Loan loan : loans) {
            if (loan.getStatus() == LoanStatus.ACTIVE) {
                activeLoans.add(loan);
            }
        }

        return activeLoans;
    }

    public List<Book> getBorrowedBooks() {

        List<Book> borrowedBooks = new ArrayList<>();

        for (Loan loan : loans) {
            if (loan.getStatus() == LoanStatus.ACTIVE) {
                borrowedBooks.add(loan.getBook());
            }
        }

        return borrowedBooks;
    }

    // Library card management

    public void assignLibraryCard(LibraryCard libraryCard) {

        if (libraryCard == null || libraryCard.getMember() != this) {
            throw new IllegalArgumentException("Invalid library card.");
        }

        this.libraryCard = libraryCard;
    }

    // Other methods

    @Override
    public String getRole() {
        return "Member";
    }

    @Override
    public String toString() {

        return "ID: " + getId()
                + " | Username: " + getUsername()
                + " | Library card: "
                + (libraryCard == null ? "Not assigned" : libraryCard.getId())
                + " | Total loans: " + loans.size()
                + " | Active loans: " + getActiveLoans().size();
    }
}