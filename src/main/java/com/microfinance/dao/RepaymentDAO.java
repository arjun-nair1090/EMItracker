package com.microfinance.dao;

import com.microfinance.config.DBConnection;
import com.microfinance.model.Repayment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the repayments table.
 */
public class RepaymentDAO {

    // ── Create ────────────────────────────────────────────────────────────────

    public boolean insertRepayment(Repayment repayment) throws SQLException {
        String sql = "INSERT INTO repayments (loan_id, emi_id, amount_paid, payment_date, status) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, repayment.getLoanId());
            ps.setInt(2, repayment.getEmiId());
            ps.setDouble(3, repayment.getAmountPaid());
            ps.setDate(4, Date.valueOf(repayment.getPaymentDate()));
            ps.setString(5, repayment.getStatus());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) repayment.setRepaymentId(rs.getInt(1));
                }
                return true;
            }
        }
        return false;
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    public List<Repayment> getRepaymentsByLoanId(int loanId) throws SQLException {
        List<Repayment> list = new ArrayList<>();
        String sql = "SELECT * FROM repayments WHERE loan_id=? ORDER BY payment_date";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, loanId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Repayment> getAllRepayments() throws SQLException {
        List<Repayment> list = new ArrayList<>();
        String sql = "SELECT * FROM repayments ORDER BY payment_date DESC";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public double getTotalRepaid(int loanId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount_paid),0) FROM repayments WHERE loan_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, loanId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        }
        return 0;
    }

    // ── Reporting helpers ─────────────────────────────────────────────────────

    public double getGrandTotalRepaid() throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount_paid),0) FROM repayments";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        }
        return 0;
    }

    // ── Row Mapper ────────────────────────────────────────────────────────────

    private Repayment mapRow(ResultSet rs) throws SQLException {
        Repayment r = new Repayment();
        r.setRepaymentId(rs.getInt("repayment_id"));
        r.setLoanId(rs.getInt("loan_id"));
        r.setEmiId(rs.getInt("emi_id"));
        r.setAmountPaid(rs.getDouble("amount_paid"));
        r.setPaymentDate(rs.getDate("payment_date").toLocalDate());
        r.setStatus(rs.getString("status"));
        return r;
    }
}
