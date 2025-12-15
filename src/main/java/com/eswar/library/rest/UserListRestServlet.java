package com.eswar.library.rest;

import com.google.gson.Gson;
import com.eswar.library.dao.UserDAO;
import com.eswar.library.dao.UserDAOImpl;
import com.eswar.library.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/users")
public class UserListRestServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAOImpl();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            List<User> users = userDAO.findAll();
            String json = gson.toJson(users);
            resp.getWriter().write(json);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Unable to fetch users\"}");
            e.printStackTrace();
        }
    }
}
