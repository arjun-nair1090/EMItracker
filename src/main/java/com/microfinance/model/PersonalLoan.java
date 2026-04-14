package com.microfinance.model;

/**
 * Personal Loan — extends LoanProduct.
 * Applies a 1% processing surcharge on the effective rate.
 */
public class PersonalLoan extends LoanProduct {

    private static final double SURCHARGE = 1.0; // extra % on top of stated rate

    public PersonalLoan() { super(); }

    public PersonalLoan(double principal, double interestRate, int tenure) {
        super(principal, interestRate, tenure);
    }

    @Override
    public double calculateEMI() {
        return emiFormula(principal, interestRate + SURCHARGE, tenure);
    }

    @Override
    public String getLoanTypeName() { return "Personal"; }

    @Override
    public String toString() {
        return String.format("[PersonalLoan] principal=₹%.2f rate=%.2f%% tenure=%d months EMI=₹%.2f",
                principal, interestRate, tenure, calculateEMI());
    }
}
