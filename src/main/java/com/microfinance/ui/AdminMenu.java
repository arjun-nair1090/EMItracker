package com.microfinance.ui;

import com.microfinance.model.*;
import com.microfinance.service.*;
import com.microfinance.dao.BorrowerDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu for logged-in Admins.
 */
public class AdminMenu {

    private final User               admin;
    private final Scanner            sc;
    private final LoanService        loanService   = new LoanService();
    private final EMIService         emiService    = new EMIService();
    private final CreditScoreService creditService = new CreditScoreService();
    private final ReportService      reportService = new ReportService();
    private final BorrowerDAO        borrowerDAO   = new BorrowerDAO();

    public AdminMenu(User admin, Scanner sc) {
        this.admin = admin;
        this.sc    = sc;
    }

    public void show() throws SQLException {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();
            System.out.println();
            if      ("1" .equals(choice)) viewAllLoans();
            else if ("2" .equals(choice)) viewPendingLoans();
            else if ("3" .equals(choice)) approveLoan();
            else if ("4" .equals(choice)) rejectLoan();
            else if ("5" .equals(choice)) searchLoans();
            else if ("6" .equals(choice)) viewAllBorrowers();
            else if ("7" .equals(choice)) viewDefaulters();
            else if ("8" .equals(choice)) runDefaulterCheck();
            else if ("9" .equals(choice)) viewEMIScheduleForLoan();
            else if ("10".equals(choice)) generateReports();
            else if ("11".equals(choice)) { System.out.println("  Logging out...\n"); running = false; }
            else System.out.println("  \u2718  Invalid option.");
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("  \u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557");
        System.out.printf ("  \u2551  ADMIN PANEL  -- %-33s\u2551%n", admin.getName() + "  ");
        System.out.println("  \u2560\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2563");
        System.out.println("  \u2551  LOANS                                           \u2551");
        System.out.println("  \u2551  [1]  View All Loans                             \u2551");
        System.out.println("  \u2551  [2]  View Pending Loans                         \u2551");
        System.out.println("  \u2551  [3]  Approve a Loan                             \u2551");
        System.out.println("  \u2551  [4]  Reject a Loan                              \u2551");
        System.out.println("  \u2551  [5]  Search / Filter Loans                      \u2551");
        System.out.println("  \u2560\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2563");
        System.out.println("  \u2551  BORROWERS                                       \u2551");
        System.out.println("  \u2551  [6]  View All Borrowers                         \u2551");
        System.out.println("  \u2551  [7]  View Defaulters                            \u2551");
        System.out.println("  \u2551  [8]  Run Defaulter Check                        \u2551");
        System.out.println("  \u2560\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2563");
        System.out.println("  \u2551  TRACKING & REPORTS                              \u2551");
        System.out.println("  \u2551  [9]  View EMI Schedule (by Loan ID)             \u2551");
        System.out.println("  \u2551  [10] Generate Full Reports                      \u2551");
        System.out.println("  \u2560\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2563");
        System.out.println("  \u2551  [11] Logout                                     \u2551");
        System.out.println("  \u255a\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255d");
        System.out.print("  -> Choice: ");
    }

    private void viewAllLoans() throws SQLException {
        printLoanList("All Loans", loanService.getAllLoans());
    }

    private void viewPendingLoans() throws SQLException {
        printLoanList("Pending Loans", loanService.getLoansByStatus("Pending"));
    }

    private void approveLoan() throws SQLException {
        System.out.print("  -> Enter Loan ID to Approve: ");
        int id = readInt(); if (id < 0) return;
        try {
            loanService.approveLoan(id);
            System.out.println("  \u2714  Loan #" + id + " approved. EMI schedule generated.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("  \u2718  " + e.getMessage());
        }
    }

    private void rejectLoan() throws SQLException {
        System.out.print("  -> Enter Loan ID to Reject: ");
        int id = readInt(); if (id < 0) return;
        try {
            loanService.rejectLoan(id);
            System.out.println("  \u2714  Loan #" + id + " rejected.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("  \u2718  " + e.getMessage());
        }
    }

    private void searchLoans() throws SQLException {
        System.out.print("  -> Borrower Name (Enter to skip): ");
        String name = sc.nextLine().trim();

        System.out.println("  -> Loan Type: [1] Personal  [2] Business  [3] Education  [Enter] Any");
        System.out.print("  -> Choice: ");
        String typeChoice = sc.nextLine().trim();
        String type;
        if      ("1".equals(typeChoice)) type = "Personal";
        else if ("2".equals(typeChoice)) type = "Business";
        else if ("3".equals(typeChoice)) type = "Education";
        else                              type = null;

        System.out.println("  -> Status: [1] Pending  [2] Approved  [3] Rejected  [Enter] Any");
        System.out.print("  -> Choice: ");
        String statusChoice = sc.nextLine().trim();
        String status;
        if      ("1".equals(statusChoice)) status = "Pending";
        else if ("2".equals(statusChoice)) status = "Approved";
        else if ("3".equals(statusChoice)) status = "Rejected";
        else                                status = null;

        List<Loan> results = loanService.searchLoans(name.isEmpty() ? null : name, type, status);
        printLoanList("Search Results", results);
    }

    private void viewAllBorrowers() throws SQLException {
        reportService.printBorrowerDetails();
    }

    private void viewDefaulters() throws SQLException {
        List<Borrower> defaulters = borrowerDAO.getDefaulters();
        System.out.println("\n  ------------ Defaulters (" + defaulters.size() + ") --------------------------");
        if (defaulters.isEmpty()) {
            System.out.println("  \u2714  No defaulters found.");
        } else {
            for (Borrower b : defaulters) {
                System.out.printf("  [%d] %-25s  Credit Score: %d%n",
                        b.getBorrowerId(), b.getName(), b.getCreditScore());
            }
        }
    }

    private void runDefaulterCheck() throws SQLException {
        System.out.println("  Running defaulter check across all borrowers...");
        creditService.runDefaulterCheckForAll();
        System.out.println("  \u2714  Done.");
    }

    private void viewEMIScheduleForLoan() throws SQLException {
        System.out.print("  -> Enter Loan ID: ");
        int id = readInt(); if (id < 0) return;
        List<EMISchedule> schedule = emiService.getSchedule(id);
        if (schedule.isEmpty()) { System.out.println("  No schedule found for Loan #" + id); return; }
        System.out.printf("  %-5s %-14s %-14s %-10s%n", "#", "Due Date", "Amount (Rs.)", "Status");
        System.out.println("  " + repeat("-", 50));
        for (EMISchedule e : schedule) e.display();
        System.out.println("  " + repeat("-", 50));
        System.out.printf("  Missed EMIs : %d%n",       emiService.countMissed(id));
        System.out.printf("  Total Repaid: Rs.%.2f%n", emiService.getTotalRepaid(id));
    }

    private void generateReports() throws SQLException {
        reportService.printFullReport();
    }

    private void printLoanList(String title, List<Loan> loans) {
        System.out.println("\n  ------------ " + title + " (" + loans.size() + ") --------------------");
        if (loans.isEmpty()) { System.out.println("  No loans found."); return; }
        System.out.printf("  %-6s %-12s %-14s %-8s %-10s %-10s%n",
                "LoanID", "Type", "Amount", "Rate%", "Tenure", "Status");
        System.out.println("  " + repeat("-", 65));
        for (Loan l : loans) {
            System.out.printf("  %-6d %-12s Rs.%-11.2f %-8.2f %-10d %-10s%n",
                    l.getLoanId(), l.getLoanType(), l.getAmount(),
                    l.getInterestRate(), l.getTenure(), l.getStatus());
        }
    }

    private int readInt() {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("  \u2718  Invalid number."); return -1; }
    }

    private String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }
}
