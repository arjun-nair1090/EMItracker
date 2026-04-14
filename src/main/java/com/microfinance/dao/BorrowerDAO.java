package com.microfinance.dao;

import com.microfinance.config.DBConnection;
import com.microfinance.model.Borrower;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the borrowers table.
 */
public class BorrowerDAO {

    // ── Create ────────────────────────────────────────────────────────────────

    public boolean insertBorrower(Borrower borrower) throws SQLException {
        String sql = "INSERT INTO borrowers (user_id, income, credit_score) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, borrower.getUserId());
            ps.setDouble(2, borrower.getIncome());
            ps.setInt(3, borrower.getCreditScore());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) borrower.setBorrowerId(rs.getInt(1));
                }
                return true;
            }
        }
        return false;
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    public Borrower getBorrowerByUserId(int userId) throws SQLException {
        String sql = "SELECT b.*, u.name, u.email, u.password FROM borrowers b "
                   + "JOIN users u ON b.user_id = u.user_id WHERE b.user_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public Borrower getBorrowerById(int borrowerId) throws SQLException {
        String sql = "SELECT b.*, u.name, u.email, u.password FROM borrowers b "
                   + "JOIN users u ON b.user_id = u.user_id WHERE b.borrower_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, borrowerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<Borrower> getAllBorrowers() throws SQLException {
        List<Borrower> list = new ArrayList<>();
        String sql = "SELECT b.*, u.name, u.email, u.password FROM borrowers b "
                   + "JOIN users u ON b.user_id = u.user_id ORDER BY b.borrower_id";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Borrower> getDefaulters() throws SQLException {
        List<Borrower> list = new ArrayList<>();
        String sql = "SELECT b.*, u.name, u.email, u.password FROM borrowers b "
                   + "JOIN users u ON b.user_id = u.user_id WHERE b.is_defaulter = 1";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ── Update ────────────────────────────────────────────────────────────────

    public boolean updateCreditScore(int borrowerId, int newScore) throws SQLException {
        String sql = "UPDATE borrowers SET credit_score=? WHERE borrower_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, newScore);
            ps.setInt(2, borrowerId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateDefaulterStatus(int borrowerId, boolean isDefaulter) throws SQLException {
        String sql = "UPDATE borrowers SET is_defaulter=? WHERE borrower_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setBoolean(1, isDefaulter);
            ps.setInt(2, borrowerId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateIncome(int borrowerId, double income) throws SQLException {
        String sql = "UPDATE borrowers SET income=? WHERE borrower_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setDouble(1, income);
            ps.setInt(2, borrowerId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Row Mapper ────────────────────────────────────────────────────────────

    private Borrower mapRow(ResultSet rs) throws SQLException {
        return new Borrower(
                rs.getInt("user_id"),
                rs.getInt("borrower_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getDouble("income"),
                rs.getInt("credit_score"),
                rs.getBoolean("is_defaulter")
        );
    }
}
