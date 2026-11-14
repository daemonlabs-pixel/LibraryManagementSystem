package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Administrator extends Account {

    private final List<Account> accounts;

    public Administrator(int id, String username, String password) {
        super(id, username, password);
        this.accounts = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "Administrator";
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public void addAccount(Account account) {

        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }

        if (account.getId() == getId()) {
            throw new IllegalArgumentException(
                "An administrator cannot add their own account."
            );
        }

        for (Account existingAccount : accounts) {
            if (existingAccount.getId() == account.getId()) {
                throw new IllegalArgumentException(
                    "An account with this ID already exists."
                );
            }
        }

        accounts.add(account);
    }

    public boolean removeAccount(int accountId) {

        if (accountId <= 0) {
            throw new IllegalArgumentException("Invalid account ID.");
        }

        for (int i = 0; i < accounts.size(); i++) {

            if (accounts.get(i).getId() == accountId) {
                accounts.remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean displayAccounts() {

        if (accounts.isEmpty()) {
            return false;
        }

        for (Account account : accounts) {
            System.out.println(
                "ID: " + account.getId()
                + " | Username: " + account.getUsername()
                + " | Role: " + account.getRole()
            );
        }

        return true;
    }
}