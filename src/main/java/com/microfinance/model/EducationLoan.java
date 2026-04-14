package com.microfinance.model;

/**
 * Education Loan — extends LoanProduct.
 * Offers a 0.5% concession on the stated interest rate.
 */
public class EducationLoan extends LoanProduct {

    private static final double CONCESSION = 0.5; // rate reduction

    public EducationLoan() { super(); }

    public EducationLoan(double principal, double interestRate, int tenure) {
        super(principal, interestRate, tenure);
    }

    @Override
    public double calculateEMI() {
        double effectiveRate = Math.max(0, interestRate - CONCESSION);
        return emiFormula(principal, effectiveRate, tenure);
    }

    @Override
    public String getLoanTypeName() { return "Education"; }

    @Override
    public String toString() {
        return String.format("[EducationLoan] principal=₹%.2f rate=%.2f%% tenure=%d months EMI=₹%.2f",
                principal, interestRate, tenure, calculateEMI());
    }
}
