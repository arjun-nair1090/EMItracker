package com.microfinance.ui;

import com.microfinance.model.Borrower;
import com.microfinance.model.User;
import com.microfinance.service.AuthService;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Login / registration screen shown on startup.
 */
public class MainMenu {

    private final Scanner     sc;
    private final AuthService authService = new AuthService();

    public MainMenu(Scanner sc) {
        this.sc = sc;
    }

    public User show() {
        printBanner();
        while (true) {
            printMenu();
            String choice = sc.nextLine().trim();
            System.out.println();
            try {
                if      ("1".equals(choice)) { User u = login();    if (u != null) return u; }
                else if ("2".equals(choice)) register();
                else if ("3".equals(choice)) {
                    System.out.println("  Goodbye! Thank you for using MicroFinance System.\n");
                    return null;
                } else {
                    System.out.println("  \u2718  Invalid option. Enter 1, 2, or 3.");
                }
            } catch (SQLException e) {
                System.out.println("  \u2718  Database error: " + e.getMessage());
            }
        }
    }

    private void printBanner() {
        System.out.println();
        System.out.println("  \u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557");
        System.out.println("  \u2551                                                       \u2551");
        System.out.println("  \u2551     MICROFINANCE & LOAN MANAGEMENT SYSTEM             \u2551");
        System.out.println("  \u2551     Java + MySQL  |  Student: Arjun Nair              \u2551");
        System.out.println("  \u2551                                                       \u2551");
        System.out.println("  \u255a\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255d");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("  +---------------------------------------+");
        System.out.println("  |  [1]  Login                           |");
        System.out.println("  |  [2]  Register as Borrower            |");
        System.out.println("  |  [3]  Exit                            |");
        System.out.println("  +---------------------------------------+");
        System.out.print("  -> Choice: ");
    }

    private User login() throws SQLException {
        System.out.println("  ---------------- Login ------------------");
        System.out.print("  -> Email   : ");
        String email = sc.nextLine().trim();
        System.out.print("  -> Password: ");
        String password = sc.nextLine().trim();

        User user = authService.login(email, password);
        if (user == null) {
            System.out.println("  \u2718  Invalid email or password. Please try again.");
            return null;
        }
        System.out.println("  \u2714  Login successful! Welcome, " + user.getName() + ".");
        return user;
    }

    private void register() throws SQLException {
        System.out.println("  ------------ Register as Borrower -------");
        System.out.print("  -> Full Name          : ");
        String name = sc.nextLine().trim();
        System.out.print("  -> Email              : ");
        String email = sc.nextLine().trim();
        System.out.print("  -> Password (min 8)   : ");
        String password = sc.nextLine().trim();
        System.out.print("  -> Monthly Income (Rs.): ");
        String incomeStr = sc.nextLine().trim();

        double income;
        try {
            income = Double.parseDouble(incomeStr);
        } catch (NumberFormatException e) {
            System.out.println("  \u2718  Income must be a valid number.");
            return;
        }

        try {
            Borrower b = authService.registerBorrower(name, email, password, income);
            if (b != null) {
                System.out.println("  \u2714  Registration successful!");
                System.out.println("     You can now log in with your email and password.");
            } else {
                System.out.println("  \u2718  Registration failed. Please try again.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("  \u2718  " + e.getMessage());
        }
    }
}
