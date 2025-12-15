package com.eswar.library.web;

import com.eswar.library.model.User;
import com.eswar.library.service.UserService;
import com.eswar.library.service.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        System.out.println("Login Attempt: " + username);
        User user = userService.login(username, password);

        if (user != null) {
            System.out.println("User Found: " + user.getUsername() + ", Role: " + user.getRole());
            HttpSession session = req.getSession();
            session.setAttribute("user", user);

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/jsp/adminDashboard.jsp");
            } else {
                resp.sendRedirect(req.getContextPath() + "/jsp/dashboard.jsp");
            }
        } else {
            System.out.println("Login Failed for: " + username);
            req.setAttribute("errorMessage", "Invalid username or password");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
        }
    }
}
