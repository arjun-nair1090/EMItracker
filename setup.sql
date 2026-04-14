-- ============================================================
--  Microfinance & Loan Management System — Database Setup
--  Run this script once to initialize the MySQL schema.
-- ============================================================

CREATE DATABASE IF NOT EXISTS microfinance_db;
USE microfinance_db;

-- ─────────────────────────────────────────────
--  1. USERS
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    user_id    INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100)                  NOT NULL,
    email      VARCHAR(150)                  NOT NULL UNIQUE,
    password   VARCHAR(255)                  NOT NULL,
    role       ENUM('admin', 'borrower')     NOT NULL DEFAULT 'borrower',
    created_at TIMESTAMP                     DEFAULT CURRENT_TIMESTAMP
);

-- ─────────────────────────────────────────────
--  2. BORROWERS
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS borrowers (
    borrower_id  INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT            NOT NULL,
    income       DECIMAL(15,2)  NOT NULL,
    credit_score INT            NOT NULL DEFAULT 700,
    is_defaulter TINYINT(1)     NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ─────────────────────────────────────────────
--  3. LOANS
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS loans (
    loan_id       INT AUTO_INCREMENT PRIMARY KEY,
    borrower_id   INT                                    NOT NULL,
    loan_type     ENUM('Personal','Business','Education') NOT NULL,
    amount        DECIMAL(15,2)                           NOT NULL,
    interest_rate DECIMAL(5,2)                            NOT NULL,
    tenure        INT                                     NOT NULL COMMENT 'tenure in months',
    emi_amount    DECIMAL(15,2)                           NOT NULL DEFAULT 0,
    status        ENUM('Pending','Approved','Rejected')   NOT NULL DEFAULT 'Pending',
    applied_at    TIMESTAMP                               DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (borrower_id) REFERENCES borrowers(borrower_id) ON DELETE CASCADE
);

-- ─────────────────────────────────────────────
--  4. EMI SCHEDULE
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS emi_schedule (
    emi_id       INT AUTO_INCREMENT PRIMARY KEY,
    loan_id      INT                        NOT NULL,
    installment  INT                        NOT NULL COMMENT 'installment number',
    due_date     DATE                       NOT NULL,
    amount       DECIMAL(15,2)              NOT NULL,
    status       ENUM('Paid','Pending')     NOT NULL DEFAULT 'Pending',
    FOREIGN KEY (loan_id) REFERENCES loans(loan_id) ON DELETE CASCADE
);

-- ─────────────────────────────────────────────
--  5. REPAYMENTS
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS repayments (
    repayment_id INT AUTO_INCREMENT PRIMARY KEY,
    loan_id      INT                        NOT NULL,
    emi_id       INT                        NOT NULL,
    amount_paid  DECIMAL(15,2)              NOT NULL,
    payment_date DATE                       NOT NULL,
    status       ENUM('Paid','Pending')     NOT NULL DEFAULT 'Paid',
    FOREIGN KEY (loan_id) REFERENCES loans(loan_id) ON DELETE CASCADE,
    FOREIGN KEY (emi_id)  REFERENCES emi_schedule(emi_id) ON DELETE CASCADE
);

-- ─────────────────────────────────────────────
--  SEED DATA — default admin account
--  Password: admin123 (stored as plain text for academic demo)
-- ─────────────────────────────────────────────
INSERT IGNORE INTO users (name, email, password, role)
VALUES ('System Admin', 'admin@microfinance.com', 'admin123', 'admin');

SELECT 'Database setup complete.' AS status;
