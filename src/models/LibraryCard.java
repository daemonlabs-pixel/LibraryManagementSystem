package models;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LibraryCard {

    private final int id;
    private final Member member;
    private final LocalDate issueDate;

    private LocalDate expirationDate;
    private boolean active;

    public LibraryCard(int id, Member member,
                       String issueDateString,
                       String expirationDateString,
                       boolean active) {

        if (id <= 0 || member == null) {
            throw new IllegalArgumentException("Invalid library card information.");
        }

        if (issueDateString == null || issueDateString.isBlank()
                || expirationDateString == null || expirationDateString.isBlank()) {
            throw new IllegalArgumentException("Issue date and expiration date are required.");
        }

        LocalDate issue;
        LocalDate expiration;

        try {
            issue = LocalDate.parse(issueDateString);
            expiration = LocalDate.parse(expirationDateString);

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format.", e);
        }

        if (!expiration.isAfter(issue)) {
            throw new IllegalArgumentException(
                "Expiration date must be after the issue date."
            );
        }

        this.id = id;
        this.member = member;
        this.issueDate = issue;
        this.expirationDate = expiration;
        this.active = active;
    }

    // Getters

    public int getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public boolean isActive() {
        return active;
    }

    // Card management

    public boolean isValid() {
        return active && !LocalDate.now().isAfter(expirationDate);
    }

    public void deactivate() {
        active = false;
    }

    public void renew(String expirationDateString) {

        if (expirationDateString == null || expirationDateString.isBlank()) {
            throw new IllegalArgumentException("Expiration date is required.");
        }

        LocalDate newExpirationDate;

        try {
            newExpirationDate = LocalDate.parse(expirationDateString);

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid expiration date.", e);
        }

        if (!newExpirationDate.isAfter(expirationDate)) {
            throw new IllegalArgumentException(
                "New expiration date must be later than the current expiration date."
            );
        }

        expirationDate = newExpirationDate;
    }

    @Override
    public String toString() {

        return "ID: " + id
                + " | Member: " + member.getUsername()
                + " | Issue date: " + issueDate
                + " | Expiration date: " + expirationDate
                + " | Active: " + active;
    }
}