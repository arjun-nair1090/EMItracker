package com.microfinance.model;

import java.time.LocalDate;

/**
 * Represents a payment made against an EMI installment.
 */
public class Repayment {

    private int       repaymentId;
    private int       loanId;
    private int       emiId;
    private double    amountPaid;
    private LocalDate paymentDate;
    private String    status;  // Paid / Pending

    // ── Constructors ──────────────────────────────────────────────────────────

    public Repayment() {}

    public Repayment(int loanId, int emiId, double amountPaid, LocalDate paymentDate) {
        this.loanId      = loanId;
        this.emiId       = emiId;
        this.amountPaid  = amountPaid;
        this.paymentDate = paymentDate;
        this.status      = "Paid";
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int       getRepaymentId()                       { return repaymentId; }
    public void      setRepaymentId(int repaymentId)        { this.repaymentId = repaymentId; }

    public int       getLoanId()                            { return loanId;  }
    public void      setLoanId(int loanId)                  { this.loanId = loanId; }

    public int       getEmiId()                             { return emiId;   }
    public void      setEmiId(int emiId)                    { this.emiId = emiId; }

    public double    getAmountPaid()                        { return amountPaid; }
    public void      setAmountPaid(double amountPaid)       { this.amountPaid = amountPaid; }

    public LocalDate getPaymentDate()                           { return paymentDate; }
    public void      setPaymentDate(LocalDate paymentDate)      { this.paymentDate = paymentDate; }

    public String    getStatus()                    { return status;  }
    public void      setStatus(String status)       { this.status = status; }

    // ── Display ───────────────────────────────────────────────────────────────

    public void display() {
        System.out.printf("  RepaymentID=%d | LoanID=%d | EMI#%d | Paid: ₹%.2f | Date: %s | [%s]%n",
                repaymentId, loanId, emiId, amountPaid, paymentDate, status);
    }

    @Override
    public String toString() {
        return String.format("[Repayment] id=%d loanId=%d emiId=%d paid=₹%.2f date=%s",
                repaymentId, loanId, emiId, amountPaid, paymentDate);
    }
}
