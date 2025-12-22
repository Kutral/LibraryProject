package com.eswar.library.servlets;

import com.eswar.library.dao.BorrowDAO;
import com.eswar.library.dao.BorrowDAOImpl;
import com.eswar.library.model.Borrow;
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

@WebServlet("/api/borrow")
public class BorrowRestServlet extends HttpServlet {

    private BorrowDAO borrowDAO = new BorrowDAOImpl();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userIdParam = req.getParameter("userId");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (userIdParam != null) {
            try {
                int userId = Integer.parseInt(userIdParam);
                List<Borrow> history = borrowDAO.findByUserId(userId);
                resp.getWriter().write(gson.toJson(new ApiResponse(true, "Borrow history fetched successfully", history)));
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Invalid user ID")));
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Missing userId parameter")));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        BufferedReader reader = req.getReader();
        try {
            JsonObject jsonBody = JsonParser.parseReader(reader).getAsJsonObject();
            if (!jsonBody.has("userId") || !jsonBody.has("bookId")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Missing userId or bookId")));
                return;
            }

            int userId = jsonBody.get("userId").getAsInt();
            int bookId = jsonBody.get("bookId").getAsInt();
            
            boolean success = borrowDAO.borrowBook(userId, bookId);
            
            if (success) {
                resp.getWriter().write(gson.toJson(new ApiResponse(true, "Book borrowed successfully")));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Failed to borrow book (maybe out of stock)")));
            }
            
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Invalid request format")));
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        BufferedReader reader = req.getReader();
        try {
            JsonObject jsonBody = JsonParser.parseReader(reader).getAsJsonObject();
            if (!jsonBody.has("borrowId")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Missing borrowId")));
                return;
            }

            int borrowId = jsonBody.get("borrowId").getAsInt();
            
            boolean success = borrowDAO.returnBook(borrowId);
            
            if (success) {
                resp.getWriter().write(gson.toJson(new ApiResponse(true, "Book returned successfully")));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Failed to return book (maybe already returned)")));
            }
            
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Invalid request format")));
            e.printStackTrace();
        }
    }
}