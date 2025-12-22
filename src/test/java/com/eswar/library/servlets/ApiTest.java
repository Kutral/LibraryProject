package com.eswar.library.servlets;

import com.eswar.library.dao.BookDAO;
import com.eswar.library.dao.UserDAO;
import com.eswar.library.model.Book;
import com.eswar.library.model.User;
import com.eswar.library.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private UserDAO userDAO;
    @Mock
    private BookDAO bookDAO;
    @Mock
    private UserService userService;

    private StringWriter responseWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException {
        responseWriter = new StringWriter();
        printWriter = new PrintWriter(responseWriter);
        lenient().when(response.getWriter()).thenReturn(printWriter);
    }

    // Helper to inject mocks via reflection
    private void inject(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testAuthLoginSuccess() throws Exception {
        AuthRestServlet servlet = new AuthRestServlet();
        inject(servlet, "userService", userService);

        when(request.getPathInfo()).thenReturn("/login");
        String json = "{\"username\":\"admin\",\"password\":\"password\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        User mockUser = new User("admin", "password", "admin@example.com");
        mockUser.setRole("ADMIN");
        when(userService.login("admin", "password")).thenReturn(mockUser);

        servlet.doPost(request, response);

        String output = responseWriter.toString();
        assertTrue(output.contains("Login successful"));
        assertTrue(output.contains("ADMIN"));
    }

    @Test
    void testAuthSignupSuccess() throws Exception {
        AuthRestServlet servlet = new AuthRestServlet();
        inject(servlet, "userService", userService);

        when(request.getPathInfo()).thenReturn("/signup");
        String json = "{\"username\":\"newuser\",\"password\":\"pass\",\"email\":\"test@test.com\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        when(userService.registerUser(any(User.class))).thenReturn(true);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        assertTrue(responseWriter.toString().contains("User registered successfully"));
    }

    @Test
    void testGetBooks() throws Exception {
        BookSearchRestServlet servlet = new BookSearchRestServlet();
        inject(servlet, "bookDAO", bookDAO);

        List<Book> books = new ArrayList<>();
        Book b1 = new Book();
        b1.setTitle("Java Programming");
        books.add(b1);

        when(bookDAO.findAll()).thenReturn(books);

        servlet.doGet(request, response);

        String output = responseWriter.toString();
        assertTrue(output.contains("Java Programming"));
        assertTrue(output.contains("true")); // success flag
    }

    @Test
    void testGetUsers() throws Exception {
        UserListRestServlet servlet = new UserListRestServlet();
        inject(servlet, "userDAO", userDAO);

        List<User> users = new ArrayList<>();
        User u1 = new User("alice", "pass", "alice@example.com");
        users.add(u1);

        when(userDAO.findAll()).thenReturn(users);

        servlet.doGet(request, response);

        String output = responseWriter.toString();
        // The UserListRestServlet returns a raw list or the structure?
        // Based on my implementation it returns raw list: gson.toJson(users)
        assertTrue(output.contains("alice"));
        assertTrue(output.contains("alice@example.com"));
    }
}
