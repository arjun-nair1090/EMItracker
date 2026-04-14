package com.microfinance.model;

/**
 * Base class for all system users.
 * Demonstrates Encapsulation and serves as the parent for Admin and Borrower.
 */
public class User {

    private int    userId;
    private String name;
    private String email;
    private String password;
    private String role;   // "admin" or "borrower"

    // ── Constructors ──────────────────────────────────────────────────────────

    public User() {}

    public User(String name, String email, String password, String role) {
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.role     = role;
    }

    public User(int userId, String name, String email, String password, String role) {
        this.userId   = userId;
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.role     = role;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int    getUserId()              { return userId;   }
    public void   setUserId(int userId)    { this.userId = userId; }

    public String getName()               { return name;    }
    public void   setName(String name)    { this.name = name; }

    public String getEmail()              { return email;   }
    public void   setEmail(String email)  { this.email = email; }

    public String getPassword()                   { return password;   }
    public void   setPassword(String password)    { this.password = password; }

    public String getRole()              { return role;   }
    public void   setRole(String role)   { this.role = role; }

    // ── Display ───────────────────────────────────────────────────────────────

    public void displayInfo() {
        System.out.println("  ID    : " + userId);
        System.out.println("  Name  : " + name);
        System.out.println("  Email : " + email);
        System.out.println("  Role  : " + role);
    }

    @Override
    public String toString() {
        return String.format("[User] id=%d name='%s' email='%s' role='%s'",
                userId, name, email, role);
    }
}
