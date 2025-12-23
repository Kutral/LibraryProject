package com.eswar.library.dao;

import com.eswar.library.model.BookRequest;
import com.eswar.library.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRequestDAOImpl implements BookRequestDAO {

    @Override
    public boolean createRequest(BookRequest request) {
        String sql = "INSERT INTO book_requests (user_id, title, author, isbn, status, request_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, request.getUserId());
            stmt.setString(2, request.getTitle());
            stmt.setString(3, request.getAuthor());
            stmt.setString(4, request.getIsbn());
            stmt.setString(5, request.getStatus()); // Should be 'PENDING' usually
            stmt.setTimestamp(6, request.getRequestDate());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<BookRequest> findByUserId(int userId) {
        List<BookRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM book_requests WHERE user_id = ? ORDER BY request_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToRequest(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public List<BookRequest> findAll() {
        List<BookRequest> requests = new ArrayList<>();
        // Join with users to get username
        String sql = "SELECT br.*, u.username FROM book_requests br JOIN users u ON br.user_id = u.id ORDER BY br.request_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                BookRequest request = mapResultSetToRequest(rs);
                request.setUsername(rs.getString("username"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE book_requests SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, id);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<BookRequest> findById(int id) {
        String sql = "SELECT * FROM book_requests WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToRequest(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private BookRequest mapResultSetToRequest(ResultSet rs) throws SQLException {
        BookRequest request = new BookRequest();
        request.setId(rs.getInt("id"));
        request.setUserId(rs.getInt("user_id"));
        request.setTitle(rs.getString("title"));
        request.setAuthor(rs.getString("author"));
        request.setIsbn(rs.getString("isbn"));
        request.setStatus(rs.getString("status"));
        request.setRequestDate(rs.getTimestamp("request_date"));
        return request;
    }
}
