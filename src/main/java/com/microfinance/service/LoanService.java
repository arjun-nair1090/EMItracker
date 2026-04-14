package com.microfinance.service;

import com.microfinance.dao.LoanDAO;
import com.microfinance.model.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic for loan lifecycle management.
 */
public class LoanService {

    private final LoanDAO            loanDAO       = new LoanDAO();
    private final EMIService         emiService    = new EMIService();
    private final CreditScoreService creditService = new CreditScoreService();

    // ── Apply for Loan ────────────────────────────────────────────────────────

    public Loan applyForLoan(int borrowerId, String loanType,
                             double amount, double interestRate, int tenure)
            throws SQLException {

        if (amount <= 0)
            throw new IllegalArgumentException("Loan amount must be greater than 0.");
        if (interestRate <= 0 || interestRate > 36)
            throw new IllegalArgumentException("Interest rate must be between 0.1% and 36%.");
        if (tenure <= 0)
            throw new IllegalArgumentException("Tenure must be a positive number of months.");

        LoanProduct product = createProduct(loanType, amount, interestRate, tenure);
        double emi = product.calculateEMI();

        Loan loan = new Loan(borrowerId, loanType, amount, interestRate, tenure, emi);
        loanDAO.insertLoan(loan);
        return loan;
    }

    // ── Admin Actions ─────────────────────────────────────────────────────────

    public boolean approveLoan(int loanId) throws SQLException {
        Loan loan = loanDAO.getLoanById(loanId);
        if (loan == null)
            throw new IllegalArgumentException("Loan not found: " + loanId);
        if (!"Pending".equals(loan.getStatus()))
            throw new IllegalStateException("Only pending loans can be approved.");
        loanDAO.updateLoanStatus(loanId, "Approved");
        emiService.generateSchedule(loan);
        return true;
    }

    public boolean rejectLoan(int loanId) throws SQLException {
        Loan loan = loanDAO.getLoanById(loanId);
        if (loan == null)
            throw new IllegalArgumentException("Loan not found: " + loanId);
        if (!"Pending".equals(loan.getStatus()))
            throw new IllegalStateException("Only pending loans can be rejected.");
        return loanDAO.updateLoanStatus(loanId, "Rejected");
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    public List<Loan> getAllLoans()                       throws SQLException { return loanDAO.getAllLoans(); }
    public List<Loan> getLoansByBorrower(int borrowerId) throws SQLException { return loanDAO.getLoansByBorrowerId(borrowerId); }
    public List<Loan> getLoansByStatus(String status)    throws SQLException { return loanDAO.getLoansByStatus(status); }
    public Loan       getLoanById(int loanId)            throws SQLException { return loanDAO.getLoanById(loanId); }

    public List<Loan> searchLoans(String name, String type, String status) throws SQLException {
        return loanDAO.searchLoans(name, type, status);
    }

    // ── Factory (replaces switch expression) ─────────────────────────────────

    private LoanProduct createProduct(String loanType, double amount, double rate, int tenure) {
        if ("Personal".equals(loanType))  return new PersonalLoan(amount, rate, tenure);
        if ("Business".equals(loanType))  return new BusinessLoan(amount, rate, tenure);
        if ("Education".equals(loanType)) return new EducationLoan(amount, rate, tenure);
        throw new IllegalArgumentException("Unknown loan type: " + loanType);
    }
}
