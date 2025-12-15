package com.eswar.library.web;

import com.eswar.library.dao.BookDAO;
import com.eswar.library.dao.BookDAOImpl;
import com.eswar.library.model.Book;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/books")
public class
BooksServlet extends HttpServlet {

    private BookDAO bookDAO = new BookDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Book> books = bookDAO.findAll();
        req.setAttribute("books", books);
        req.getRequestDispatcher("/jsp/books.jsp").forward(req, resp);
    }
}
