package com.microfinance.service;

import com.microfinance.dao.BorrowerDAO;
import com.microfinance.dao.UserDAO;
import com.microfinance.model.Borrower;
import com.microfinance.model.User;

import java.sql.SQLException;

/**
 * Handles user registration and login.
 */
public class AuthService {

    private final UserDAO     userDAO     = new UserDAO();
    private final BorrowerDAO borrowerDAO = new BorrowerDAO();

    // ── Registration ──────────────────────────────────────────────────────────

    /**
     * Register a new borrower.
     * @return the created Borrower, or null on failure
     */
    public Borrower registerBorrower(String name, String email, String password, double income)
            throws SQLException {

        // Validate inputs
        if (name == null || name.trim().isEmpty())       throw new IllegalArgumentException("Name cannot be empty.");
        if (!isValidEmail(email))                      throw new IllegalArgumentException("Invalid email format.");
        if (password == null || password.length() < 8) throw new IllegalArgumentException("Password must be at least 8 characters.");
        if (income <= 0)                               throw new IllegalArgumentException("Income must be greater than 0.");
        if (userDAO.emailExists(email))                throw new IllegalArgumentException("An account with this email already exists.");

        // Create user record
        Borrower borrower = new Borrower(name, email, password, income);
        if (!userDAO.insertUser(borrower)) return null;

        // Create borrower record linked to user
        if (!borrowerDAO.insertBorrower(borrower)) return null;
        return borrower;
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    /**
     * Authenticate a user and return the appropriate User object.
     * @return User (or Borrower subtype) if credentials match, null otherwise
     */
    public User login(String email, String password) throws SQLException {
        if (email == null || password == null) return null;
        User user = userDAO.authenticate(email, password);
        if (user == null) return null;

        if ("borrower".equals(user.getRole())) {
            // Return enriched Borrower object
            return borrowerDAO.getBorrowerByUserId(user.getUserId());
        }
        return user; // Admin
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }
}
