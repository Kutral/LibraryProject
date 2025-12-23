package com.eswar.library.servlets;

import com.eswar.library.dao.BookRequestDAO;
import com.eswar.library.dao.BookRequestDAOImpl;
import com.eswar.library.model.BookRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/requests")
public class BookRequestServlet extends HttpServlet {

    private BookRequestDAO bookRequestDAO = new BookRequestDAOImpl();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            BufferedReader reader = req.getReader();
            BookRequest newRequest = gson.fromJson(reader, BookRequest.class);

            if (newRequest == null || newRequest.getTitle() == null || newRequest.getUserId() == 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Missing required fields (userId, title)")));
                return;
            }

            // Default status
            newRequest.setStatus("PENDING");
            // Date is set in constructor or DB default, but let's ensure it's handled.
            if (newRequest.getRequestDate() == null) {
                newRequest.setRequestDate(new java.sql.Timestamp(System.currentTimeMillis()));
            }

            boolean created = bookRequestDAO.createRequest(newRequest);
            if (created) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write(gson.toJson(new ApiResponse(true, "Request submitted successfully")));
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Failed to submit request")));
            }
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Invalid JSON format")));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Server Error")));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String userIdParam = req.getParameter("userId");
        if (userIdParam != null) {
            try {
                int userId = Integer.parseInt(userIdParam);
                List<BookRequest> requests = bookRequestDAO.findByUserId(userId);
                resp.getWriter().write(gson.toJson(requests));
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Invalid userId format")));
            }
        } else {
            // If no userId, maybe list all? Or error? 
            // The blueprint says /api/requests?userId={id} for "Returns a list of requests for a specific user"
            // Accessing all requests is for Admin.
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Missing userId parameter")));
        }
    }
}
