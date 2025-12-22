package com.eswar.library.servlets;

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
public class AdminStatsRestServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Mock stats for now as we don't have a StatsService
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBooks", 120);
        stats.put("activeUsers", 45);
        stats.put("booksBorrowed", 12);

        resp.getWriter().write(gson.toJson(new ApiResponse(true, "Stats fetched", stats)));
    }
}
