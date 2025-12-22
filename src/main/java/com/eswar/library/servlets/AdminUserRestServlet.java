package com.eswar.library.servlets;

import com.eswar.library.dao.UserDAO;
import com.eswar.library.dao.UserDAOImpl;
import com.eswar.library.model.User;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/admin/users")
public class AdminUserRestServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAOImpl();
    private Gson gson = new Gson();

    // UPDATE User
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            BufferedReader reader = req.getReader();
            User userToUpdate = gson.fromJson(reader, User.class);

            if (userToUpdate.getId() == 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Missing user ID")));
                return;
            }

            boolean updated = userDAO.updateUser(userToUpdate);
            if (updated) {
                resp.getWriter().write(gson.toJson(new ApiResponse(true, "User updated successfully")));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "User not found or update failed")));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Invalid request format")));
        }
    }

    // DELETE User
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String idParam = req.getParameter("id");
            if (idParam == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Missing user ID")));
                return;
            }

            int id = Integer.parseInt(idParam);
            boolean deleted = userDAO.deleteUser(id);

            if (deleted) {
                resp.getWriter().write(gson.toJson(new ApiResponse(true, "User deleted successfully")));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "User not found or delete failed")));
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Invalid ID format")));
        }
    }
}
