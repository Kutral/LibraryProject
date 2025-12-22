package com.eswar.library.servlets;

import com.eswar.library.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/seed-books")
public class SeedBooksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");

        String sql = "INSERT INTO books (title, author, isbn, available_copies) VALUES (?, ?, ?, ?)";
        
        Object[][] books = {
            {"The Hobbit", "J.R.R. Tolkien", "9780547928227", 7},
            {"Fahrenheit 451", "Ray Bradbury", "9781451673319", 4},
            {"Moby Dick", "Herman Melville", "9781503280786", 2},
            {"War and Peace", "Leo Tolstoy", "9781400079988", 3},
            {"The Odyssey", "Homer", "9780140268867", 5},
            {"Hamlet", "William Shakespeare", "9780743477123", 6},
            {"The Catcher in the Rye", "J.D. Salinger", "9780316769480", 4},
            {"The Brothers Karamazov", "Fyodor Dostoevsky", "9780374528379", 3},
            {"Crime and Punishment", "Fyodor Dostoevsky", "9780486415871", 4},
            {"Brave New World", "Aldous Huxley", "9780060850524", 5}
        };

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int count = 0;
            for (Object[] book : books) {
                // Generate a random suffix for ISBN to ensure uniqueness if run multiple times
                String randomIsbnSuffix = String.valueOf((int)(Math.random() * 10000));
                
                stmt.setString(1, (String) book[0]);
                stmt.setString(2, (String) book[1]);
                stmt.setString(3, book[2] + "-" + randomIsbnSuffix); 
                stmt.setInt(4, (Integer) book[3]);
                stmt.addBatch();
                count++;
            }
            
            int[] results = stmt.executeBatch();
            resp.getWriter().println("Successfully added " + results.length + " new books to the library!");
            
        } catch (Exception e) {
            e.printStackTrace(resp.getWriter());
        }
    }
}
