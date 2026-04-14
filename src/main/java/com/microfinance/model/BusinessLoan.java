package com.microfinance.model;

/**
 * Business Loan — extends LoanProduct.
 * Uses the base interest rate directly (no surcharge).
 */
public class BusinessLoan extends LoanProduct {

    public BusinessLoan() { super(); }

    public BusinessLoan(double principal, double interestRate, int tenure) {
        super(principal, interestRate, tenure);
    }

    @Override
    public double calculateEMI() {
        return emiFormula(principal, interestRate, tenure);
    }

    @Override
    public String getLoanTypeName() { return "Business"; }

    @Override
    public String toString() {
        return String.format("[BusinessLoan] principal=₹%.2f rate=%.2f%% tenure=%d months EMI=₹%.2f",
                principal, interestRate, tenure, calculateEMI());
    }
}
