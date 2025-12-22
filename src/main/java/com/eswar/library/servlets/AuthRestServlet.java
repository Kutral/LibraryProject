package com.eswar.library.servlets;

import com.eswar.library.model.User;
import com.eswar.library.service.UserService;
import com.eswar.library.service.UserServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/auth/*")
public class AuthRestServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("AuthRestServlet initialized!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            if (pathInfo == null || pathInfo.equals("/")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Invalid endpoint")));
                return;
            }

            BufferedReader reader = req.getReader();
            JsonObject jsonBody = JsonParser.parseReader(reader).getAsJsonObject();

            if ("/login".equals(pathInfo)) {
                handleLogin(jsonBody, resp);
            } else if ("/signup".equals(pathInfo)) {
                handleSignup(jsonBody, resp);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "Endpoint not found")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Server Error: " + e.getMessage())));
        }
    }

    private void handleLogin(JsonObject json, HttpServletResponse resp) throws IOException {
        String username = json.has("username") ? json.get("username").getAsString() : null;
        String password = json.has("password") ? json.get("password").getAsString() : null;

        if (username == null || password == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Missing username or password")));
            return;
        }

        User user = userService.login(username, password);
        if (user != null) {

            user.setPassword(null);
            resp.getWriter().write(gson.toJson(new ApiResponse(true, "Login successful", user)));
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Invalid credentials")));
        }
    }

    private void handleSignup(JsonObject json, HttpServletResponse resp) throws IOException {
        String username = json.has("username") ? json.get("username").getAsString() : null;
        String password = json.has("password") ? json.get("password").getAsString() : null;
        String email = json.has("email") ? json.get("email").getAsString() : null;

        if (username == null || password == null || email == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Missing required fields")));
            return;
        }

        User newUser = new User(username, password, email);
        boolean registered = userService.registerUser(newUser);

        if (registered) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(new ApiResponse(true, "User registered successfully")));
        } else {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "Username or email already exists")));
        }
    }
}
