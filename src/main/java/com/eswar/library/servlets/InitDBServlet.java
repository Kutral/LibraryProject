package com.eswar.library.servlets;

import com.eswar.library.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

@WebServlet("/init-db")
public class InitDBServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");

        String[] sqlStatements = {
            "CREATE TABLE IF NOT EXISTS users (" +
            "    id INT AUTO_INCREMENT PRIMARY KEY," +
            "    username VARCHAR(50) NOT NULL UNIQUE," +
            "    password VARCHAR(255) NOT NULL," +
            "    email VARCHAR(100) NOT NULL UNIQUE," +
            "    role VARCHAR(20) DEFAULT 'USER'," +
            "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")",
            
            "CREATE TABLE IF NOT EXISTS books (" +
            "    id INT AUTO_INCREMENT PRIMARY KEY," +
            "    title VARCHAR(255) NOT NULL," +
            "    author VARCHAR(255) NOT NULL," +
            "    isbn VARCHAR(20) UNIQUE," +
            "    available_copies INT DEFAULT 1" +
            ")",
            
            "CREATE TABLE IF NOT EXISTS borrowing_history (" +
            "    id INT AUTO_INCREMENT PRIMARY KEY," +
            "    user_id INT NOT NULL," +
            "    book_id INT NOT NULL," +
            "    borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "    return_date TIMESTAMP NULL," +
            "    FOREIGN KEY (user_id) REFERENCES users(id)," +
            "    FOREIGN KEY (book_id) REFERENCES books(id)" +
            ")",

            "CREATE TABLE IF NOT EXISTS book_requests (" +
            "    id INT AUTO_INCREMENT PRIMARY KEY," +
            "    user_id INT NOT NULL," +
            "    title VARCHAR(255) NOT NULL," +
            "    author VARCHAR(255)," +
            "    isbn VARCHAR(50)," +
            "    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING'," +
            "    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
            ")",

            // Seed initial data
            "INSERT IGNORE INTO books (id, title, author, isbn, available_copies) VALUES " +
            "(1, 'The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 5)," +
            "(2, 'To Kill a Mockingbird', 'Harper Lee', '9780061120084', 3)," +
            "(3, '1984', 'George Orwell', '9780451524935', 4)," +
            "(4, 'Pride and Prejudice', 'Jane Austen', '9780141439518', 2)," +
            "(5, 'The Catcher in the Rye', 'J.D. Salinger', '9780316769480', 3)",

            "INSERT IGNORE INTO users (id, username, password, email, role) VALUES " +
            "(1, 'admin', 'admin', 'admin@library.com', 'ADMIN')"
        };

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            for (String sql : sqlStatements) {
                stmt.execute(sql);
            }
            resp.getWriter().println("Database Initialized Successfully via API!");
            
        } catch (Exception e) {
            e.printStackTrace(resp.getWriter());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
