package services;

import models.Book;
import models.Member;
import models.Loan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Library {

    private final int id;
    private final String name;

    private final List<Book> books;
    private final List<Member> members;
    private final List<Loan> loans;

    public Library(int id, String name) {

        if (id <= 0 || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid library information.");
        }

        this.id = id;
        this.name = name;

        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addBook(Book book) {

        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }

        if (findBook(book.getId()) != null) {
            throw new IllegalArgumentException("A book with this ID already exists.");
        }

        books.add(book);
    }

    public boolean removeBook(int bookId) {

        if (bookId <= 0) {
            return false;
        }

        Book book = findBook(bookId);

        if (book == null) {
            return false;
        }

        // Do not remove a book that is currently borrowed.
        for (Loan loan : loans) {
            if (loan.getBook() == book
                    && loan.getStatus() == enums.LoanStatus.ACTIVE) {
                return false;
            }
        }

        return books.remove(book);
    }

    public Book findBook(int bookId) {

        if (bookId <= 0) {
            throw new IllegalArgumentException("Invalid book ID.");
        }

        for (Book book : books) {
            if (book.getId() == bookId) {
                return book;
            }
        }

        return null;
    }

    public void addMember(Member member) {

        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }

        if (findMember(member.getId()) != null) {
            throw new IllegalArgumentException("A member with this ID already exists.");
        }

        members.add(member);
    }

    public boolean removeMember(int memberId) {

        if (memberId <= 0) {
            return false;
        }

        Member member = findMember(memberId);

        if (member == null) {
            return false;
        }

        // Preserve the member's loan history.
        if (!member.getLoans().isEmpty()) {
            return false;
        }

        return members.remove(member);
    }

    public Member findMember(int memberId) {

        if (memberId <= 0) {
            throw new IllegalArgumentException("Invalid member ID.");
        }

        for (Member member : members) {
            if (member.getId() == memberId) {
                return member;
            }
        }

        return null;
    }

    public void addLoan(Loan loan) {

        if (loan == null) {
            throw new IllegalArgumentException("Loan cannot be null.");
        }

        if (findLoan(loan.getId()) != null) {
            throw new IllegalArgumentException("A loan with this ID already exists.");
        }

        if (findMember(loan.getMember().getId()) != loan.getMember()
                || findBook(loan.getBook().getId()) != loan.getBook()) {
            throw new IllegalArgumentException(
                "The loan's member and book must belong to this library."
            );
        }

        loans.add(loan);
    }

    public Loan findLoan(int loanId) {

        if (loanId <= 0) {
            throw new IllegalArgumentException("Invalid loan ID.");
        }

        for (Loan loan : loans) {
            if (loan.getId() == loanId) {
                return loan;
            }
        }

        return null;
    }

    public boolean removeLoan(int loanId) {

        if (loanId <= 0) {
            return false;
        }

        Loan loan = findLoan(loanId);

        if (loan == null) {
            return false;
        }

        // Keep the member's loan history consistent.
        loan.getMember().removeLoan(loanId);

        return loans.remove(loan);
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public List<Loan> getLoans() {
        return Collections.unmodifiableList(loans);
    }

    public int generateLoanId() {

        int max = 0;

        for (Loan loan : loans) {
            if (loan.getId() > max) {
                max = loan.getId();
            }
        }

        return max + 1;
    }

    @Override
    public String toString() {

        return "Library ID: " + id
                + " | Library name: " + name
                + " | Number of books: " + books.size()
                + " | Number of members: " + members.size()
                + " | Number of loans: " + loans.size();
    }
}