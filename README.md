
# Library Management System

A console-based Library Management System developed in Java using Object-Oriented Programming (OOP) principles.

The project aims to simulate the basic operations of a library, including account management, book inventory management, borrowing, returning, and loan tracking.

## Features

### Account Management
- Abstract account structure.
- Administrator, Librarian, and Member roles.
- Basic authentication.
- Account registration and removal.

### Book Management
- Add new books.
- Remove books from the inventory.
- Update total and available copies.
- Display available books.
- Prevent invalid copy counts.

### Borrowing and Returning
- Borrow books using member and book IDs.
- Return borrowed books.
- Check library card validity.
- Limit members to a maximum of three active loans.
- Set a borrowing period of 14 days.

### Loan Management
- Track active and returned loans.
- Maintain member loan history.
- Identify overdue loans.
- Calculate late fees.

## Technologies

- Java
- Object-Oriented Programming
- Java Collections Framework
- Java Time API
- Git and GitHub

## Project Structure

```text
LibraryManagementSystem/
│
├── src/
│   ├── enums/
│   │   └── LoanStatus.java
│   │
│   ├── interfaces/
│   │   └── Borrowable.java
│   │
│   ├── models/
│   │   ├── Account.java
│   │   ├── Administrator.java
│   │   ├── Book.java
│   │   ├── Librarian.java
│   │   ├── LibraryCard.java
│   │   ├── Loan.java
│   │   └── Member.java
│   │
│   ├── services/
│   │   ├── Library.java
│   │   └── LibraryManager.java
│   │
│   └── Main.java
│
├── .gitignore
└── README.md
```

## Getting Started

### Prerequisites

- Java Development Kit (JDK 17 or later).
- A Java-compatible IDE or code editor.
- Git (optional).

### Installation

1. Clone the repository:

```bash
git clone <repository-url>
```

2. Navigate to the project directory:

```bash
cd LibraryManagementSystem
```

3. Compile the source files:

```bash
javac -d out src/Main.java src/models/*.java src/services/*.java src/interfaces/*.java src/enums/*.java
```

4. Run the application:

```bash
java -cp out Main
```

## Application Access

The application currently uses sample accounts initialized in `Main.java`.

| Role | Username / ID | Password |
|---|---|---|
| Administrator | admin | admin123 |
| Librarian | librarian | lib123 |
| Member | 3 | member123 |

These are demonstration credentials and should not be used in a production environment.

## Current Limitations

- Data is stored in memory and is lost when the application terminates.
- Authentication uses basic password comparison.
- Account management and persistence are still limited.
- The application currently operates through a command-line interface.

## Learning Objectives

This project is intended to practice:

- Encapsulation.
- Inheritance.
- Abstraction.
- Polymorphism.
- Interfaces.
- Exception handling.
- Collections and object relationships.
- Separation of responsibilities between classes.

## Future Improvements

- Database integration using JDBC and MySQL.
- Persistent account and book management.
- Password hashing and improved authentication.
- More advanced administrative features.
- Improved input validation.
- Automated unit testing.

## Author

Developed as a personal Java learning project.
