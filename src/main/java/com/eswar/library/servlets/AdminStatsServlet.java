package com.eswar.library.servlets;

import com.eswar.library.dao.BookDAO;
import com.eswar.library.dao.BookDAOImpl;
import com.eswar.library.dao.BorrowDAO;
import com.eswar.library.dao.BorrowDAOImpl;
import com.eswar.library.dao.UserDAO;
import com.eswar.library.dao.UserDAOImpl;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/admin/stats")
public class AdminStatsServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAOImpl();
    private BookDAO bookDAO = new BookDAOImpl();
    private BorrowDAO borrowDAO = new BorrowDAOImpl();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            int totalUsers = userDAO.countUsers();
            int totalBooks = bookDAO.countBooks();
            int activeBorrows = borrowDAO.countActiveBorrows();

            Map<String, Integer> stats = new HashMap<>();
            stats.put("totalUsers", totalUsers);
            stats.put("totalBooks", totalBooks);
            stats.put("activeBorrows", activeBorrows);

            resp.getWriter().write(gson.toJson(new ApiResponse(true, "Stats fetched successfully", stats)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Failed to fetch stats")));
            e.printStackTrace();
        }
    }
}
