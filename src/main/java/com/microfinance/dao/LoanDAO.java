package com.microfinance.dao;

import com.microfinance.config.DBConnection;
import com.microfinance.model.Loan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the loans table.
 */
public class LoanDAO {

    // ── Create ────────────────────────────────────────────────────────────────

    public boolean insertLoan(Loan loan) throws SQLException {
        String sql = "INSERT INTO loans (borrower_id, loan_type, amount, interest_rate, tenure, emi_amount, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, 'Pending')";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, loan.getBorrowerId());
            ps.setString(2, loan.getLoanType());
            ps.setDouble(3, loan.getAmount());
            ps.setDouble(4, loan.getInterestRate());
            ps.setInt(5, loan.getTenure());
            ps.setDouble(6, loan.getEmiAmount());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) loan.setLoanId(rs.getInt(1));
                }
                return true;
            }
        }
        return false;
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    public Loan getLoanById(int loanId) throws SQLException {
        String sql = "SELECT * FROM loans WHERE loan_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, loanId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<Loan> getLoansByBorrowerId(int borrowerId) throws SQLException {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE borrower_id = ? ORDER BY loan_id";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, borrowerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Loan> getAllLoans() throws SQLException {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loans ORDER BY loan_id";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Loan> getLoansByStatus(String status) throws SQLException {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE status = ? ORDER BY loan_id";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Loan> searchLoans(String borrowerName, String loanType, String status) throws SQLException {
        List<Loan> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT l.* FROM loans l JOIN borrowers b ON l.borrower_id = b.borrower_id "
              + "JOIN users u ON b.user_id = u.user_id WHERE 1=1");
        if (borrowerName != null && !borrowerName.trim().isEmpty())
            sql.append(" AND u.name LIKE '%" + borrowerName.replace("'","''") + "%'");
        if (loanType != null && !loanType.trim().isEmpty())
            sql.append(" AND l.loan_type='" + loanType.replace("'","''") + "'");
        if (status != null && !status.trim().isEmpty())
            sql.append(" AND l.status='" + status.replace("'","''") + "'");
        sql.append(" ORDER BY l.loan_id");
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql.toString())) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ── Update ────────────────────────────────────────────────────────────────

    public boolean updateLoanStatus(int loanId, String status) throws SQLException {
        String sql = "UPDATE loans SET status=? WHERE loan_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, loanId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    public boolean deleteLoan(int loanId) throws SQLException {
        String sql = "DELETE FROM loans WHERE loan_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, loanId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Row Mapper ────────────────────────────────────────────────────────────

    private Loan mapRow(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setLoanId(rs.getInt("loan_id"));
        loan.setBorrowerId(rs.getInt("borrower_id"));
        loan.setLoanType(rs.getString("loan_type"));
        loan.setAmount(rs.getDouble("amount"));
        loan.setInterestRate(rs.getDouble("interest_rate"));
        loan.setTenure(rs.getInt("tenure"));
        loan.setEmiAmount(rs.getDouble("emi_amount"));
        loan.setStatus(rs.getString("status"));
        return loan;
    }
}
