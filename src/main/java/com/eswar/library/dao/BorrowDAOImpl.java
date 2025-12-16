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
        // Simple mock implementation that doesn't actually check constraints for now,
        // or assumes DB constraints handle it.
        String sql = "INSERT INTO borrowing_history (user_id, book_id, borrow_date) VALUES (?, ?, NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean returnBook(int borrowId) {
        String sql = "UPDATE borrowing_history SET return_date = NOW() WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, borrowId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Borrow> findByUserId(int userId) {
        List<Borrow> list = new ArrayList<>();
        String sql = "SELECT * FROM borrowing_history WHERE user_id = ?";
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
                    list.add(b);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
