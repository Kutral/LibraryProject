package com.eswar.library.servlets;

import com.eswar.library.dao.BookDAO;
import com.eswar.library.dao.BookDAOImpl;
import com.eswar.library.model.Book;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/admin/books")
public class AdminBookRestServlet extends HttpServlet {

    private BookDAO bookDAO = new BookDAOImpl();
    private Gson gson = new Gson();

    // CREATE Book
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            BufferedReader reader = req.getReader();
            Book newBook = gson.fromJson(reader, Book.class);

            if (newBook.getTitle() == null || newBook.getAuthor() == null || newBook.getIsbn() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Missing required fields")));
                return;
            }

            boolean created = bookDAO.createBook(newBook);
            if (created) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write(gson.toJson(new ApiResponse(true, "Book created successfully")));
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Failed to create book")));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Invalid request format")));
        }
    }

    // UPDATE Book
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            BufferedReader reader = req.getReader();
            Book bookToUpdate = gson.fromJson(reader, Book.class);

            if (bookToUpdate.getId() == 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Missing book ID")));
                return;
            }

            boolean updated = bookDAO.updateBook(bookToUpdate);
            if (updated) {
                resp.getWriter().write(gson.toJson(new ApiResponse(true, "Book updated successfully")));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Book not found or update failed")));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Invalid request format")));
        }
    }

    // DELETE Book
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String idParam = req.getParameter("id");
            if (idParam == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Missing book ID")));
                return;
            }

            int id = Integer.parseInt(idParam);
            boolean deleted = bookDAO.deleteBook(id);

            if (deleted) {
                resp.getWriter().write(gson.toJson(new ApiResponse(true, "Book deleted successfully")));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Book not found or delete failed")));
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Invalid ID format")));
        }
    }
}
