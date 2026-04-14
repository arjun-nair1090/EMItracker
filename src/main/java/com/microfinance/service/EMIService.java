package com.microfinance.service;

import com.microfinance.dao.EMIScheduleDAO;
import com.microfinance.dao.RepaymentDAO;
import com.microfinance.model.EMISchedule;
import com.microfinance.model.Loan;
import com.microfinance.model.Repayment;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * EMI calculation engine and schedule/repayment management.
 *
 * Demonstrates Method Overloading — three versions of calculateEMI().
 */
public class EMIService {

    private final EMIScheduleDAO scheduleDAO  = new EMIScheduleDAO();
    private final RepaymentDAO   repaymentDAO = new RepaymentDAO();

    // ══════════════════════════════════════════════════════════════════════════
    //  Method Overloading — Polymorphism (Compile-time)
    // ══════════════════════════════════════════════════════════════════════════

    /** Calculate EMI using default rate (10%) and tenure (12 months). */
    public double calculateEMI(double principal) {
        return emiFormula(principal, 10.0, 12);
    }

    /** Calculate EMI using default tenure of 12 months. */
    public double calculateEMI(double principal, double annualRate) {
        return emiFormula(principal, annualRate, 12);
    }

    /** Full EMI calculation with all parameters. */
    public double calculateEMI(double principal, double annualRate, int tenureMonths) {
        return emiFormula(principal, annualRate, tenureMonths);
    }

    // ── Core formula ──────────────────────────────────────────────────────────

    private double emiFormula(double principal, double annualRate, int tenureMonths) {
        if (annualRate == 0) return principal / tenureMonths;
        double r     = annualRate / 12.0 / 100.0;
        double power = Math.pow(1 + r, tenureMonths);
        return (principal * r * power) / (power - 1);
    }

    // ── Schedule Generation ───────────────────────────────────────────────────

    /**
     * Generates the full EMI installment schedule for an approved loan.
     * Each installment is a row in emi_schedule starting from next month.
     */
    public void generateSchedule(Loan loan) throws SQLException {
        LocalDate dueDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        for (int i = 1; i <= loan.getTenure(); i++) {
            EMISchedule entry = new EMISchedule(loan.getLoanId(), i, dueDate, loan.getEmiAmount());
            scheduleDAO.insertEMI(entry);
            dueDate = dueDate.plusMonths(1);
        }
    }

    // ── Repayment ─────────────────────────────────────────────────────────────

    /**
     * Records a payment against the oldest pending EMI for a loan.
     * @return true if payment recorded, false if no pending EMIs
     */
    public boolean recordPayment(int loanId) throws SQLException {
        List<EMISchedule> pending = scheduleDAO.getPendingByLoanId(loanId);
        if (pending.isEmpty()) return false;

        EMISchedule next = pending.get(0);   // pay the earliest pending installment
        Repayment rp = new Repayment(loanId, next.getEmiId(), next.getAmount(), LocalDate.now());
        repaymentDAO.insertRepayment(rp);
        scheduleDAO.markAsPaid(next.getEmiId());
        return true;
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    public List<EMISchedule> getSchedule(int loanId)  throws SQLException { return scheduleDAO.getScheduleByLoanId(loanId); }
    public List<EMISchedule> getPending(int loanId)   throws SQLException { return scheduleDAO.getPendingByLoanId(loanId); }
    public int  countMissed(int loanId)               throws SQLException { return scheduleDAO.countMissedEMIs(loanId); }
    public List<Repayment> getRepayments(int loanId)  throws SQLException { return repaymentDAO.getRepaymentsByLoanId(loanId); }
    public double getTotalRepaid(int loanId)           throws SQLException { return repaymentDAO.getTotalRepaid(loanId); }
}
