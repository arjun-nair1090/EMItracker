package com.microfinance.service;

import com.microfinance.config.DBConnection;
import com.microfinance.dao.BorrowerDAO;
import com.microfinance.dao.LoanDAO;
import com.microfinance.dao.RepaymentDAO;
import com.microfinance.model.Borrower;
import com.microfinance.model.Loan;

import java.sql.*;
import java.util.List;

/**
 * Generates admin-level aggregate reports using SQL aggregation.
 */
public class ReportService {

    private final LoanDAO      loanDAO      = new LoanDAO();
    private final BorrowerDAO  borrowerDAO  = new BorrowerDAO();
    private final RepaymentDAO repaymentDAO = new RepaymentDAO();

    // ── Full Report ───────────────────────────────────────────────────────────

    public void printFullReport() throws SQLException {
        printSeparator("MICROFINANCE SYSTEM — ADMIN REPORT");
        printLoanSummary();
        printLoanTypeDistribution();
        printRepaymentsSummary();
        printDefaulters();
        printSeparator("");
    }

    // ── Loan Summary ──────────────────────────────────────────────────────────

    public void printLoanSummary() throws SQLException {
        String sql = "SELECT status, COUNT(*) AS count, COALESCE(SUM(amount),0) AS total "
                   + "FROM loans GROUP BY status";
        System.out.println("\n  ┌─ Loan Summary ─────────────────────────────────┐");
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            int grandCount = 0; double grandTotal = 0;
            while (rs.next()) {
                String status = rs.getString("status");
                int count     = rs.getInt("count");
                double total  = rs.getDouble("total");
                grandCount   += count;
                grandTotal   += total;
                System.out.printf("  │  %-10s : %3d loans   ₹%,12.2f%n", status, count, total);
            }
            System.out.println("  │  ──────────────────────────────────────────────");
            System.out.printf("  │  %-10s : %3d loans   ₹%,12.2f%n", "TOTAL", grandCount, grandTotal);
        }
        System.out.println("  └────────────────────────────────────────────────┘");
    }

    // ── Loan Type Distribution ────────────────────────────────────────────────

    public void printLoanTypeDistribution() throws SQLException {
        String sql = "SELECT loan_type, COUNT(*) AS count, COALESCE(SUM(amount),0) AS total "
                   + "FROM loans WHERE status='Approved' GROUP BY loan_type";
        System.out.println("\n  ┌─ Loan Distribution (Approved) ─────────────────┐");
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("  │  %-10s : %3d loans   ₹%,12.2f%n",
                        rs.getString("loan_type"), rs.getInt("count"), rs.getDouble("total"));
            }
        }
        System.out.println("  └────────────────────────────────────────────────┘");
    }

    // ── Repayments Summary ────────────────────────────────────────────────────

    public void printRepaymentsSummary() throws SQLException {
        double total = repaymentDAO.getGrandTotalRepaid();
        System.out.println("\n  ┌─ Repayment Summary ────────────────────────────┐");
        System.out.printf("  │  Total Repayments Collected : ₹%,12.2f%n", total);
        System.out.println("  └────────────────────────────────────────────────┘");
    }

    // ── Defaulters ────────────────────────────────────────────────────────────

    public void printDefaulters() throws SQLException {
        List<Borrower> defaulters = borrowerDAO.getDefaulters();
        System.out.println("\n  ┌─ Defaulters (" + defaulters.size() + ") ──────────────────────────────────┐");
        if (defaulters.isEmpty()) {
            System.out.println("  │  No defaulters found. ✓");
        } else {
            for (Borrower b : defaulters) {
                System.out.printf("  │  [%d] %-25s  Credit: %d%n",
                        b.getBorrowerId(), b.getName(), b.getCreditScore());
            }
        }
        System.out.println("  └────────────────────────────────────────────────┘");
    }

    // ── Borrower Detail ───────────────────────────────────────────────────────

    public void printBorrowerDetails() throws SQLException {
        List<Borrower> borrowers = borrowerDAO.getAllBorrowers();
        System.out.println("\n  ┌─ All Borrowers ─────────────────────────────────┐");
        for (Borrower b : borrowers) {
            System.out.printf("  │  [%d] %-20s  Income: ₹%,9.2f  Credit: %d  %s%n",
                    b.getBorrowerId(), b.getName(), b.getIncome(),
                    b.getCreditScore(), b.isDefaulter() ? "⚠ DEFAULTER" : "");
        }
        System.out.println("  └────────────────────────────────────────────────┘");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void printSeparator(String title) {
        System.out.println();
        System.out.println("  ═══════════════════════════════════════════════════");
        if (!title.isEmpty()) System.out.println("  " + title);
        System.out.println("  ═══════════════════════════════════════════════════");
    }
}
