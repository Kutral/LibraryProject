package com.eswar.library.servlets;

import com.eswar.library.dao.BookDAO;
import com.eswar.library.dao.BookDAOImpl;
import com.eswar.library.dao.BookRequestDAO;
import com.eswar.library.dao.BookRequestDAOImpl;
import com.eswar.library.dao.UserDAO;
import com.eswar.library.dao.UserDAOImpl;
import com.eswar.library.model.Book;
import com.eswar.library.model.BookRequest;
import com.eswar.library.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/api/admin/requests/*")
public class AdminBookRequestServlet extends HttpServlet {

    private BookRequestDAO bookRequestDAO = new BookRequestDAOImpl();
    private BookDAO bookDAO = new BookDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();
    private Gson gson = new Gson();

    // Helper method to check Admin role
    private boolean isAdmin(HttpServletRequest req) {
        String userIdStr = req.getHeader("X-User-ID");
        if (userIdStr == null) return false;
        try {
            int userId = Integer.parseInt(userIdStr);
            Optional<User> userOpt = userDAO.findById(userId);
            return userOpt.isPresent() && "ADMIN".equalsIgnoreCase(userOpt.get().getRole());
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Basic Admin Security Check
        if (!isAdmin(req)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Access Denied: Admin role required")));
            return;
        }

        List<BookRequest> requests = bookRequestDAO.findAll();
        resp.getWriter().write(gson.toJson(requests));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (!isAdmin(req)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Access Denied: Admin role required")));
            return;
        }
        
        // Extract ID from path
        String pathInfo = req.getPathInfo(); // /123
        if (pathInfo == null || pathInfo.length() <= 1) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Missing request ID")));
            return;
        }

        try {
            int requestId = Integer.parseInt(pathInfo.substring(1));
            
            BufferedReader reader = req.getReader();
            JsonObject jsonBody = JsonParser.parseReader(reader).getAsJsonObject();
            String status = jsonBody.has("status") ? jsonBody.get("status").getAsString() : null;

            if (status == null || (!status.equals("APPROVED") && !status.equals("REJECTED"))) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Invalid status (must be APPROVED or REJECTED)")));
                return;
            }

            Optional<BookRequest> requestOpt = bookRequestDAO.findById(requestId);
            if (requestOpt.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Request not found")));
                return;
            }

            BookRequest request = requestOpt.get();
            
            // If already processed, maybe don't do anything? Or allow update?
            // Assuming allow update.

            boolean updated = bookRequestDAO.updateStatus(requestId, status);
            if (updated) {
                // If Approved and wasn't already approved, add to books
                if ("APPROVED".equals(status) && !"APPROVED".equals(request.getStatus())) {
                    // Logic: Add to books table
                    Book newBook = new Book();
                    newBook.setTitle(request.getTitle());
                    newBook.setAuthor(request.getAuthor());
                    newBook.setIsbn(request.getIsbn());
                    newBook.setAvailableCopies(1);
                    
                    try {
                        bookDAO.createBook(newBook);
                        // We might want to handle duplication error if ISBN exists.
                        // But BookDAO might just fail or throw exception.
                    } catch (Exception e) {
                        System.err.println("Failed to auto-create book: " + e.getMessage());
                        // We still consider the status update successful? Yes.
                    }
                }
                
                resp.getWriter().write(gson.toJson(new ApiResponse(true, "Request updated successfully")));
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Failed to update request")));
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Invalid ID format")));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Server Error")));
        }
    }
}
