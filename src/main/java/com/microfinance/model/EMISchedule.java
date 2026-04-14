package com.microfinance.model;

import java.time.LocalDate;

/**
 * Represents a single EMI installment in the loan schedule.
 */
public class EMISchedule {

    private int       emiId;
    private int       loanId;
    private int       installment;  // installment number (1, 2, 3, ...)
    private LocalDate dueDate;
    private double    amount;
    private String    status;       // Paid / Pending

    // ── Constructors ──────────────────────────────────────────────────────────

    public EMISchedule() {}

    public EMISchedule(int loanId, int installment, LocalDate dueDate, double amount) {
        this.loanId      = loanId;
        this.installment = installment;
        this.dueDate     = dueDate;
        this.amount      = amount;
        this.status      = "Pending";
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int       getEmiId()                     { return emiId;      }
    public void      setEmiId(int emiId)            { this.emiId = emiId; }

    public int       getLoanId()                    { return loanId;     }
    public void      setLoanId(int loanId)          { this.loanId = loanId; }

    public int       getInstallment()               { return installment; }
    public void      setInstallment(int installment){ this.installment = installment; }

    public LocalDate getDueDate()                   { return dueDate;    }
    public void      setDueDate(LocalDate dueDate)  { this.dueDate = dueDate; }

    public double    getAmount()                    { return amount;     }
    public void      setAmount(double amount)       { this.amount = amount; }

    public String    getStatus()                    { return status;     }
    public void      setStatus(String status)       { this.status = status; }

    // ── Display ───────────────────────────────────────────────────────────────

    public void display() {
        System.out.printf("  #%-3d | Due: %s | ₹%10.2f | [%s]%n",
                installment, dueDate, amount, status);
    }

    @Override
    public String toString() {
        return String.format("[EMI] id=%d loanId=%d #%d due=%s amount=₹%.2f status=%s",
                emiId, loanId, installment, dueDate, amount, status);
    }
}
