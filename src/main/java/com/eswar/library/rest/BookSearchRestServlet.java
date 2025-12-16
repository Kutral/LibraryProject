package com.eswar.library.rest;

import com.eswar.library.dao.BookDAO;
import com.eswar.library.dao.BookDAOImpl;
import com.eswar.library.model.Book;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/books")
public class BookSearchRestServlet extends HttpServlet {

    private BookDAO bookDAO = new BookDAOImpl();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            List<Book> books = bookDAO.findAll();
            resp.getWriter().write(gson.toJson(new ApiResponse(true, "Books fetched successfully", books)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Error fetching books")));
            e.printStackTrace();
        }
    }

    private static class ApiResponse {
        boolean success;
        String message;
        Object data;

        ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
    }
}
