package com.microfinance.model;

import java.time.LocalDateTime;

/**
 * Represents an active loan record in the database.
 */
public class Loan {

    private int            loanId;
    private int            borrowerId;
    private String         loanType;      // Personal / Business / Education
    private double         amount;
    private double         interestRate;
    private int            tenure;        // months
    private double         emiAmount;
    private String         status;        // Pending / Approved / Rejected
    private LocalDateTime  appliedAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Loan() {}

    public Loan(int borrowerId, String loanType, double amount,
                double interestRate, int tenure, double emiAmount) {
        this.borrowerId   = borrowerId;
        this.loanType     = loanType;
        this.amount       = amount;
        this.interestRate = interestRate;
        this.tenure       = tenure;
        this.emiAmount    = emiAmount;
        this.status       = "Pending";
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int            getLoanId()                      { return loanId;      }
    public void           setLoanId(int loanId)            { this.loanId = loanId; }

    public int            getBorrowerId()                  { return borrowerId;  }
    public void           setBorrowerId(int borrowerId)    { this.borrowerId = borrowerId; }

    public String         getLoanType()                    { return loanType;    }
    public void           setLoanType(String loanType)     { this.loanType = loanType; }

    public double         getAmount()                      { return amount;      }
    public void           setAmount(double amount)         { this.amount = amount; }

    public double         getInterestRate()                        { return interestRate; }
    public void           setInterestRate(double interestRate)     { this.interestRate = interestRate; }

    public int            getTenure()               { return tenure;  }
    public void           setTenure(int tenure)     { this.tenure = tenure; }

    public double         getEmiAmount()                   { return emiAmount;   }
    public void           setEmiAmount(double emiAmount)   { this.emiAmount = emiAmount; }

    public String         getStatus()                   { return status;  }
    public void           setStatus(String status)      { this.status = status; }

    public LocalDateTime  getAppliedAt()                        { return appliedAt;   }
    public void           setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    // ── Display ───────────────────────────────────────────────────────────────

    public void display() {
        System.out.printf("  Loan ID       : %d%n",       loanId);
        System.out.printf("  Type          : %s%n",       loanType);
        System.out.printf("  Amount        : ₹%.2f%n",   amount);
        System.out.printf("  Interest Rate : %.2f%%%n",  interestRate);
        System.out.printf("  Tenure        : %d months%n", tenure);
        System.out.printf("  Monthly EMI   : ₹%.2f%n",   emiAmount);
        System.out.printf("  Status        : %s%n",       status);
    }

    @Override
    public String toString() {
        return String.format("[Loan] id=%d type=%s amount=₹%.2f rate=%.2f%% tenure=%d status=%s",
                loanId, loanType, amount, interestRate, tenure, status);
    }
}
