package com.microfinance.model;

/**
 * Represents an Admin user.
 * Inherits from User — demonstrates Inheritance.
 */
public class Admin extends User {

    public Admin() {
        super();
        setRole("admin");
    }

    public Admin(String name, String email, String password) {
        super(name, email, password, "admin");
    }

    public Admin(int userId, String name, String email, String password) {
        super(userId, name, email, password, "admin");
    }

    @Override
    public void displayInfo() {
        System.out.println("  ── Admin Account ──");
        super.displayInfo();
    }

    @Override
    public String toString() {
        return "[Admin] " + super.toString();
    }
}
