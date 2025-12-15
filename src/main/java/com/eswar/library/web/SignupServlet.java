package com.eswar.library.web;

import com.eswar.library.model.User;
import com.eswar.library.service.UserService;
import com.eswar.library.service.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");


        User newUser = new User(username, password, email);

        boolean isRegistered = userService.registerUser(newUser);


        if (isRegistered) {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?signupSuccess=true");
        } else {
            req.setAttribute("errorMessage", "Signup failed (Username/Email might already exist).");
            req.getRequestDispatcher("/jsp/signup.jsp").forward(req, resp);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/signup.jsp").forward(req, resp);
    }
}