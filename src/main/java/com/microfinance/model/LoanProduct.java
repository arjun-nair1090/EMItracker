package com.microfinance.model;

/**
 * Abstract base class for all loan products.
 * Demonstrates Abstraction — subclasses must implement calculateEMI().
 */
public abstract class LoanProduct {

    protected double principal;
    protected double interestRate; // annual rate in %
    protected int    tenure;       // months

    // ── Constructors ──────────────────────────────────────────────────────────

    public LoanProduct() {}

    public LoanProduct(double principal, double interestRate, int tenure) {
        this.principal    = principal;
        this.interestRate = interestRate;
        this.tenure       = tenure;
    }

    // ── Abstract Method (overridden by each loan type) ────────────────────────

    /**
     * Calculate the monthly EMI for this loan product.
     * Each subclass overrides this with its own formula/rate logic.
     */
    public abstract double calculateEMI();

    // ── Standard EMI formula (static utility, used by subclasses) ────────────

    /**
     * EMI = [P × R × (1+R)^N] / [(1+R)^N − 1]
     * where R = monthly interest rate, N = tenure in months.
     */
    protected static double emiFormula(double principal, double annualRate, int tenureMonths) {
        if (annualRate == 0) return principal / tenureMonths;
        double r = annualRate / 12.0 / 100.0;
        double power = Math.pow(1 + r, tenureMonths);
        return (principal * r * power) / (power - 1);
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public double getPrincipal()                    { return principal;    }
    public void   setPrincipal(double principal)    { this.principal = principal; }

    public double getInterestRate()                         { return interestRate; }
    public void   setInterestRate(double interestRate)      { this.interestRate = interestRate; }

    public int    getTenure()               { return tenure;  }
    public void   setTenure(int tenure)     { this.tenure = tenure; }

    public abstract String getLoanTypeName();
}
