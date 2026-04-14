package com.microfinance.dao;

import com.microfinance.config.DBConnection;
import com.microfinance.model.EMISchedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the emi_schedule table.
 */
public class EMIScheduleDAO {

    // ── Create ────────────────────────────────────────────────────────────────

    public boolean insertEMI(EMISchedule emi) throws SQLException {
        String sql = "INSERT INTO emi_schedule (loan_id, installment, due_date, amount, status) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, emi.getLoanId());
            ps.setInt(2, emi.getInstallment());
            ps.setDate(3, Date.valueOf(emi.getDueDate()));
            ps.setDouble(4, emi.getAmount());
            ps.setString(5, emi.getStatus());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) emi.setEmiId(rs.getInt(1));
                }
                return true;
            }
        }
        return false;
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    public List<EMISchedule> getScheduleByLoanId(int loanId) throws SQLException {
        List<EMISchedule> list = new ArrayList<>();
        String sql = "SELECT * FROM emi_schedule WHERE loan_id=? ORDER BY installment";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, loanId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<EMISchedule> getPendingByLoanId(int loanId) throws SQLException {
        List<EMISchedule> list = new ArrayList<>();
        String sql = "SELECT * FROM emi_schedule WHERE loan_id=? AND status='Pending' ORDER BY installment";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, loanId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public int countMissedEMIs(int loanId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM emi_schedule WHERE loan_id=? AND status='Pending' AND due_date < CURDATE()";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, loanId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    // ── Update ────────────────────────────────────────────────────────────────

    public boolean markAsPaid(int emiId) throws SQLException {
        String sql = "UPDATE emi_schedule SET status='Paid' WHERE emi_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, emiId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    public boolean deleteScheduleByLoanId(int loanId) throws SQLException {
        String sql = "DELETE FROM emi_schedule WHERE loan_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, loanId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Row Mapper ────────────────────────────────────────────────────────────

    private EMISchedule mapRow(ResultSet rs) throws SQLException {
        EMISchedule e = new EMISchedule();
        e.setEmiId(rs.getInt("emi_id"));
        e.setLoanId(rs.getInt("loan_id"));
        e.setInstallment(rs.getInt("installment"));
        e.setDueDate(rs.getDate("due_date").toLocalDate());
        e.setAmount(rs.getDouble("amount"));
        e.setStatus(rs.getString("status"));
        return e;
    }
}
