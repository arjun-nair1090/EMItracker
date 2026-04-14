package com.microfinance.model;

/**
 * Represents a Borrower in the system.
 * Extends User — demonstrates Inheritance.
 */
public class Borrower extends User {

    private int    borrowerId;
    private double income;
    private int    creditScore;
    private boolean isDefaulter;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Borrower() {
        super();
        setRole("borrower");
        this.creditScore = 700;
        this.isDefaulter = false;
    }

    public Borrower(String name, String email, String password, double income) {
        super(name, email, password, "borrower");
        this.income      = income;
        this.creditScore = 700;
        this.isDefaulter = false;
    }

    public Borrower(int userId, int borrowerId, String name, String email,
                    String password, double income, int creditScore, boolean isDefaulter) {
        super(userId, name, email, password, "borrower");
        this.borrowerId  = borrowerId;
        this.income      = income;
        this.creditScore = creditScore;
        this.isDefaulter = isDefaulter;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int     getBorrowerId()                   { return borrowerId;   }
    public void    setBorrowerId(int borrowerId)     { this.borrowerId = borrowerId; }

    public double  getIncome()                       { return income;  }
    public void    setIncome(double income)          { this.income = income; }

    public int     getCreditScore()                  { return creditScore; }
    public void    setCreditScore(int creditScore)   { this.creditScore = creditScore; }

    public boolean isDefaulter()                     { return isDefaulter; }
    public void    setDefaulter(boolean defaulter)   { this.isDefaulter = defaulter; }

    // ── Display ───────────────────────────────────────────────────────────────

    @Override
    public void displayInfo() {
        System.out.println("  ── Borrower Profile ──");
        super.displayInfo();
        System.out.printf("  Income       : ₹%.2f%n", income);
        System.out.println("  Credit Score : " + creditScore);
        System.out.println("  Defaulter    : " + (isDefaulter ? "YES ⚠" : "No"));
    }

    @Override
    public String toString() {
        return String.format("[Borrower] borrowerId=%d userId=%d name='%s' credit=%d",
                borrowerId, getUserId(), getName(), creditScore);
    }
}
