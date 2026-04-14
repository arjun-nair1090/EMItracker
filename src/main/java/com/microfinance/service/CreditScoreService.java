package com.microfinance.service;

import com.microfinance.dao.BorrowerDAO;
import com.microfinance.dao.LoanDAO;
import com.microfinance.model.Borrower;
import com.microfinance.model.Loan;

import java.sql.SQLException;
import java.util.List;

/**
 * Manages credit score calculation and defaulter detection.
 *
 * Scoring Rules:
 *   Starting Score      : 700
 *   Successful Payment  : +5
 *   Missed EMI          : -10
 *   Full Loan Repaid    : +20
 *
 * Defaulter Threshold: missed EMIs >= 3 on any active loan
 */
public class CreditScoreService {

    private static final int STARTING_SCORE        = 700;
    private static final int PAYMENT_REWARD        = +5;
    private static final int MISSED_EMI_PENALTY    = -10;
    private static final int FULL_REPAYMENT_REWARD = +20;
    private static final int DEFAULTER_THRESHOLD   = 3;

    private final BorrowerDAO borrowerDAO = new BorrowerDAO();
    private final LoanDAO     loanDAO     = new LoanDAO();
    private final EMIService  emiService  = new EMIService();

    // ── Score Updates ─────────────────────────────────────────────────────────

    /** Call after a successful EMI payment. */
    public void onPaymentSuccess(int borrowerId) throws SQLException {
        adjustScore(borrowerId, PAYMENT_REWARD, "Successful payment (+5)");
    }

    /** Call after a full loan is repaid. */
    public void onLoanFullyRepaid(int borrowerId) throws SQLException {
        adjustScore(borrowerId, FULL_REPAYMENT_REWARD, "Loan fully repaid (+20)");
    }

    /** Call when an EMI is missed. */
    public void onMissedEMI(int borrowerId) throws SQLException {
        adjustScore(borrowerId, MISSED_EMI_PENALTY, "Missed EMI (-10)");
    }

    // ── Defaulter Detection ───────────────────────────────────────────────────

    /**
     * Scans all approved loans for this borrower.
     * Flags them as defaulter if any loan has >= DEFAULTER_THRESHOLD missed EMIs today.
     */
    public void evaluateDefaulterStatus(int borrowerId) throws SQLException {
        List<Loan> loans = loanDAO.getLoansByBorrowerId(borrowerId);
        boolean shouldBeDefaulter = false;
        for (Loan loan : loans) {
            if ("Approved".equals(loan.getStatus())) {
                int missed = emiService.countMissed(loan.getLoanId());
                if (missed >= DEFAULTER_THRESHOLD) {
                    shouldBeDefaulter = true;
                    break;
                }
            }
        }
        borrowerDAO.updateDefaulterStatus(borrowerId, shouldBeDefaulter);
    }

    /**
     * Run defaulter evaluation across all borrowers (admin batch operation).
     */
    public void runDefaulterCheckForAll() throws SQLException {
        List<Borrower> borrowers = borrowerDAO.getAllBorrowers();
        for (Borrower b : borrowers) {
            evaluateDefaulterStatus(b.getBorrowerId());
        }
        System.out.println("  Defaulter check completed for " + borrowers.size() + " borrowers.");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void adjustScore(int borrowerId, int delta, String reason) throws SQLException {
        Borrower borrower = borrowerDAO.getBorrowerById(borrowerId);
        if (borrower == null) throw new IllegalArgumentException("Borrower not found: " + borrowerId);
        int newScore = Math.max(300, Math.min(900, borrower.getCreditScore() + delta));
        borrowerDAO.updateCreditScore(borrowerId, newScore);
        System.out.printf("  Credit Score: %d → %d  (%s)%n", borrower.getCreditScore(), newScore, reason);
    }
}
