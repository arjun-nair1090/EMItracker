package com.microfinance;

import com.microfinance.config.DBConnection;
import com.microfinance.model.Borrower;
import com.microfinance.model.User;
import com.microfinance.ui.AdminMenu;
import com.microfinance.ui.BorrowerMenu;
import com.microfinance.ui.MainMenu;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Microfinance & Loan Management System — Entry Point
 *
 * Prerequisites:
 *   1. MySQL running on localhost:3306
 *   2. Run setup.sql once to create the database and tables
 *   3. Update DB_PASSWORD in DBConnection.java to match your MySQL password
 *   4. Run build.bat to compile and start
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Verify DB connection on startup
        try {
            DBConnection.getConnection();
            System.out.println("  \u2714  Connected to microfinance_db");
        } catch (SQLException e) {
            System.out.println("  \u2718  Cannot connect to database: " + e.getMessage());
            System.out.println("     -> Ensure MySQL is running and setup.sql has been executed.");
            System.out.println("     -> Update DB_PASSWORD in DBConnection.java if needed.");
            sc.close();
            return;
        }

        try {
            MainMenu mainMenu = new MainMenu(sc);

            while (true) {
                // Show login/register screen; returns null on exit
                User user = mainMenu.show();
                if (user == null) break;   // user chose Exit

                // Route to the appropriate menu based on role
                if ("admin".equals(user.getRole())) {
                    new AdminMenu(user, sc).show();
                } else if (user instanceof Borrower) {
                    new BorrowerMenu((Borrower) user, sc).show();
                } else {
                    System.out.println("  \u2718  Unknown role: " + user.getRole());
                }
            }

        } catch (SQLException e) {
            System.out.println("\n  \u2718  Fatal database error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close();
            sc.close();
            System.out.println("  Connection closed. Goodbye!");
        }
    }
}
