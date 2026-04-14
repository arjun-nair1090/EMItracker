package com.microfinance.ui;

import com.microfinance.model.Borrower;
import com.microfinance.model.Loan;
import com.microfinance.model.EMISchedule;
import com.microfinance.model.Repayment;
import com.microfinance.service.CreditScoreService;
import com.microfinance.service.EMIService;
import com.microfinance.service.LoanService;
import com.microfinance.dao.UserDAO;
import com.microfinance.dao.BorrowerDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu for logged-in Borrowers.
 */
public class BorrowerMenu {

    private final Borrower           borrower;
    private final Scanner            sc;
    private final LoanService        loanService   = new LoanService();
    private final EMIService         emiService    = new EMIService();
    private final CreditScoreService creditService = new CreditScoreService();
    private final UserDAO            userDAO       = new UserDAO();
    private final BorrowerDAO        borrowerDAO   = new BorrowerDAO();

    public BorrowerMenu(Borrower borrower, Scanner sc) {
        this.borrower = borrower;
        this.sc       = sc;
    }

    public void show() throws SQLException {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();
            System.out.println();
            if      ("1".equals(choice)) viewProfile();
            else if ("2".equals(choice)) applyForLoan();
            else if ("3".equals(choice)) viewMyLoans();
            else if ("4".equals(choice)) viewEMISchedule();
            else if ("5".equals(choice)) makePayment();
            else if ("6".equals(choice)) viewRepaymentHistory();
            else if ("7".equals(choice)) viewCreditScore();
            else if ("8".equals(choice)) updateProfile();
            else if ("9".equals(choice)) { System.out.println("  Logging out...\n"); running = false; }
            else System.out.println("  \u2718  Invalid option. Please try again.");
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("  \u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557");
        System.out.printf ("  \u2551  Welcome, %-38s\u2551%n", borrower.getName() + "  ");
        System.out.println("  \u2560\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2563");
        System.out.println("  \u2551  [1]  View My Profile                            \u2551");
        System.out.println("  \u2551  [2]  Apply for a Loan                           \u2551");
        System.out.println("  \u2551  [3]  View My Loans                              \u2551");
        System.out.println("  \u2551  [4]  View EMI Schedule                          \u2551");
        System.out.println("  \u2551  [5]  Make an EMI Payment                        \u2551");
        System.out.println("  \u2551  [6]  Repayment History                          \u2551");
        System.out.println("  \u2551  [7]  Check Credit Score                         \u2551");
        System.out.println("  \u2551  [8]  Update Profile                             \u2551");
        System.out.println("  \u2551  [9]  Logout                                     \u2551");
        System.out.println("  \u255a\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255d");
        System.out.print("  -> Choice: ");
    }

    private void viewProfile() {
        System.out.println("  ---------------- My Profile --------------------");
        borrower.displayInfo();
        System.out.println("  -------------------------------------------------");
    }

    private void applyForLoan() throws SQLException {
        System.out.println("  ------------ Apply for a Loan -------------------");
        System.out.println("  Loan Types: [1] Personal   [2] Business   [3] Education");
        System.out.print("  -> Select type: ");
        String typeChoice = sc.nextLine().trim();
        String loanType;
        if      ("1".equals(typeChoice)) loanType = "Personal";
        else if ("2".equals(typeChoice)) loanType = "Business";
        else if ("3".equals(typeChoice)) loanType = "Education";
        else { System.out.println("  \u2718  Invalid loan type."); return; }

        double amount, rate;
        int    tenure;
        try {
            System.out.print("  -> Loan Amount (Rs.): ");
            amount = Double.parseDouble(sc.nextLine().trim());
            System.out.print("  -> Annual Interest Rate (%): ");
            rate   = Double.parseDouble(sc.nextLine().trim());
            System.out.print("  -> Tenure (months): ");
            tenure = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  \u2718  Invalid number entered."); return;
        }

        try {
            Loan loan = loanService.applyForLoan(borrower.getBorrowerId(), loanType, amount, rate, tenure);
            System.out.println("  \u2714  Loan application submitted successfully!");
            System.out.printf ("     Loan ID    : %d%n",      loan.getLoanId());
            System.out.printf ("     Monthly EMI: Rs.%.2f%n", loan.getEmiAmount());
            System.out.println("     Status     : Pending (awaiting admin approval)");
        } catch (IllegalArgumentException e) {
            System.out.println("  \u2718  " + e.getMessage());
        }
    }

    private void viewMyLoans() throws SQLException {
        List<Loan> loans = loanService.getLoansByBorrower(borrower.getBorrowerId());
        System.out.println("  ---------------- My Loans -----------------------");
        if (loans.isEmpty()) { System.out.println("  No loans found."); return; }
        for (Loan l : loans) {
            System.out.println();
            l.display();
            System.out.println("  ---------------------------------------------");
        }
    }

    private void viewEMISchedule() throws SQLException {
        int loanId = selectLoanId("view EMI schedule for");
        if (loanId < 0) return;

        List<EMISchedule> schedule = emiService.getSchedule(loanId);
        if (schedule.isEmpty()) {
            System.out.println("  No EMI schedule found. (Loan may still be pending approval)");
            return;
        }
        double totalRepaid = emiService.getTotalRepaid(loanId);
        System.out.printf("  %-5s %-14s %-14s %-10s%n", "#", "Due Date", "Amount (Rs.)", "Status");
        System.out.println("  " + repeat("-", 50));
        for (EMISchedule e : schedule) e.display();
        System.out.println("  " + repeat("-", 50));
        System.out.printf("  Total Repaid So Far: Rs.%.2f%n", totalRepaid);
    }

    private void makePayment() throws SQLException {
        int loanId = selectLoanId("make a payment on");
        if (loanId < 0) return;

        List<EMISchedule> pending = emiService.getPending(loanId);
        if (pending.isEmpty()) {
            System.out.println("  \u2714  No pending EMIs - this loan is fully repaid!");
            creditService.onLoanFullyRepaid(borrower.getBorrowerId());
            return;
        }
        EMISchedule next = pending.get(0);
        System.out.printf("  Next EMI: #%d | Due: %s | Amount: Rs.%.2f%n",
                next.getInstallment(), next.getDueDate(), next.getAmount());
        System.out.print("  -> Confirm payment? (y/n): ");
        String confirm = sc.nextLine().trim();
        if (!confirm.equalsIgnoreCase("y")) { System.out.println("  Payment cancelled."); return; }

        boolean success = emiService.recordPayment(loanId);
        if (success) {
            System.out.printf("  \u2714  Payment of Rs.%.2f recorded successfully!%n", next.getAmount());
            creditService.onPaymentSuccess(borrower.getBorrowerId());
            creditService.evaluateDefaulterStatus(borrower.getBorrowerId());
        } else {
            System.out.println("  \u2718  Payment failed. Please try again.");
        }
    }

    private void viewRepaymentHistory() throws SQLException {
        int loanId = selectLoanId("view repayment history for");
        if (loanId < 0) return;

        List<Repayment> history = emiService.getRepayments(loanId);
        System.out.println("  ------------ Repayment History ------------------");
        if (history.isEmpty()) { System.out.println("  No payments made yet."); return; }
        for (Repayment r : history) r.display();
    }

    private void viewCreditScore() {
        System.out.println("  ------------ Credit Score -----------------------");
        int score = borrower.getCreditScore();
        String rating;
        if      (score >= 800) rating = "Excellent";
        else if (score >= 700) rating = "Good";
        else if (score >= 600) rating = "Fair";
        else                   rating = "Poor";
        System.out.printf("  Score  : %d / 900%n", score);
        System.out.printf("  Rating : %s%n", rating);
        System.out.printf("  Status : %s%n", borrower.isDefaulter() ? "DEFAULTER" : "Good Standing");
        System.out.println("  -------------------------------------------------");
    }

    private void updateProfile() throws SQLException {
        System.out.println("  ------------ Update Profile ---------------------");
        System.out.print("  -> New Name (Enter to skip): ");
        String name = sc.nextLine().trim();
        System.out.print("  -> New Password (Enter to skip): ");
        String pass = sc.nextLine().trim();
        System.out.print("  -> New Income (0 to skip): ");
        String incomeStr = sc.nextLine().trim();

        if (!name.isEmpty())  borrower.setName(name);
        if (!pass.isEmpty()) {
            if (pass.length() < 8) { System.out.println("  \u2718  Password too short (min 8 chars)."); return; }
            borrower.setPassword(pass);
        }
        userDAO.updateUser(borrower);

        if (!incomeStr.isEmpty() && !"0".equals(incomeStr)) {
            try {
                double income = Double.parseDouble(incomeStr);
                if (income > 0) borrowerDAO.updateIncome(borrower.getBorrowerId(), income);
            } catch (NumberFormatException e) {
                System.out.println("  \u2718  Invalid income value.");
            }
        }
        System.out.println("  \u2714  Profile updated successfully.");
    }

    private int selectLoanId(String action) throws SQLException {
        List<Loan> loans = loanService.getLoansByBorrower(borrower.getBorrowerId());
        if (loans.isEmpty()) { System.out.println("  You have no loans."); return -1; }
        System.out.println("  Your Loans:");
        for (Loan l : loans)
            System.out.printf("    [%d] %s - Rs.%.2f (%s)%n",
                    l.getLoanId(), l.getLoanType(), l.getAmount(), l.getStatus());
        System.out.print("  -> Enter Loan ID to " + action + ": ");
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  \u2718  Invalid Loan ID.");
            return -1;
        }
    }

    private String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }
}
