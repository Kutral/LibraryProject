package com.eswar.library.dao;

import com.eswar.library.model.Borrow;
import com.eswar.library.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAOImpl implements BorrowDAO {

    @Override
    public boolean borrowBook(int userId, int bookId) {
        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement checkActiveStmt = null;
        PreparedStatement updateBookStmt = null;
        PreparedStatement insertBorrowStmt = null;
        ResultSet rs = null;
        ResultSet rsActive = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 0. Check if user already
            String checkActiveSql = "SELECT count(*) FROM borrowing_history WHERE user_id = ? AND book_id = ? AND return_date IS NULL";
            checkActiveStmt = conn.prepareStatement(checkActiveSql);
            checkActiveStmt.setInt(1, userId);
            checkActiveStmt.setInt(2, bookId);
            rsActive = checkActiveStmt.executeQuery();
            if (rsActive.next() && rsActive.getInt(1) > 0) {
                System.out.println("DEBUG: User " + userId + " already has book " + bookId + " borrowed.");
                conn.rollback();
                return false;
            }

            // 1. Check availability
            String checkSql = "SELECT available_copies FROM books WHERE id = ? FOR UPDATE";
            checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, bookId);
            rs = checkStmt.executeQuery();

            if (rs.next()) {
                int copies = rs.getInt("available_copies");
                System.out.println("DEBUG: Book ID " + bookId + " found. Copies available: " + copies);
                
                if (copies > 0) {
                    // 2. Decrement copies
                    String updateBookSql = "UPDATE books SET available_copies = available_copies - 1 WHERE id = ?";
                    updateBookStmt = conn.prepareStatement(updateBookSql);
                    updateBookStmt.setInt(1, bookId);
                    int updated = updateBookStmt.executeUpdate();
                    System.out.println("DEBUG: Decremented copies. Rows affected: " + updated);

                    // 3. Insert borrow record
                    String insertBorrowSql = "INSERT INTO borrowing_history (user_id, book_id, borrow_date) VALUES (?, ?, NOW())";
                    insertBorrowStmt = conn.prepareStatement(insertBorrowSql);
                    insertBorrowStmt.setInt(1, userId);
                    insertBorrowStmt.setInt(2, bookId);
                    int inserted = insertBorrowStmt.executeUpdate();
                    System.out.println("DEBUG: Inserted borrow record. Rows affected: " + inserted);

                    conn.commit();
                    return true;
                } else {
                    System.out.println("DEBUG: No copies available for Book ID " + bookId);
                }
            } else {
                System.out.println("DEBUG: Book ID " + bookId + " not found in database.");
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            System.err.println("Transaction Error: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (rsActive != null) rsActive.close();
                if (checkStmt != null) checkStmt.close();
                if (checkActiveStmt != null) checkActiveStmt.close();
                if (updateBookStmt != null) updateBookStmt.close();
                if (insertBorrowStmt != null) insertBorrowStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean returnBook(int borrowId) {
        Connection conn = null;
        PreparedStatement getBorrowStmt = null;
        PreparedStatement updateBorrowStmt = null;
        PreparedStatement updateBookStmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Get book_id and check if already returned
            String getBorrowSql = "SELECT book_id, return_date FROM borrowing_history WHERE id = ?";
            getBorrowStmt = conn.prepareStatement(getBorrowSql);
            getBorrowStmt.setInt(1, borrowId);
            rs = getBorrowStmt.executeQuery();

            if (rs.next()) {
                if (rs.getTimestamp("return_date") != null) {

                    conn.rollback();
                    return false;
                }
                int bookId = rs.getInt("book_id");

                // 2. Update borrow record
                String updateBorrowSql = "UPDATE borrowing_history SET return_date = NOW() WHERE id = ?";
                updateBorrowStmt = conn.prepareStatement(updateBorrowSql);
                updateBorrowStmt.setInt(1, borrowId);
                updateBorrowStmt.executeUpdate();

                // 3. Increment book copies
                String updateBookSql = "UPDATE books SET available_copies = available_copies + 1 WHERE id = ?";
                updateBookStmt = conn.prepareStatement(updateBookSql);
                updateBookStmt.setInt(1, bookId);
                updateBookStmt.executeUpdate();

                conn.commit();
                return true;
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            System.err.println("Transaction Error: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (getBorrowStmt != null) getBorrowStmt.close();
                if (updateBorrowStmt != null) updateBorrowStmt.close();
                if (updateBookStmt != null) updateBookStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Borrow> findByUserId(int userId) {
        List<Borrow> list = new ArrayList<>();
        // Join with books table to get title and author
        String sql = "SELECT bh.*, b.title, b.author FROM borrowing_history bh " +
                     "JOIN books b ON bh.book_id = b.id " +
                     "WHERE bh.user_id = ? ORDER BY bh.borrow_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Borrow b = new Borrow();
                    b.setId(rs.getInt("id"));
                    b.setUserId(rs.getInt("user_id"));
                    b.setBookId(rs.getInt("book_id"));
                    b.setBorrowDate(rs.getTimestamp("borrow_date"));
                    b.setReturnDate(rs.getTimestamp("return_date"));

                    b.setBookTitle(rs.getString("title"));
                    b.setBookAuthor(rs.getString("author"));
                    list.add(b);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}