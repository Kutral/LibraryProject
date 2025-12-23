package com.eswar.library.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/test-dashboard")
public class TestDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>API Test Dashboard</title>");
        out.println("<style>");
        out.println("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 20px; background-color: #f9f9f9; }");
        out.println(".section { border: 1px solid #ddd; padding: 20px; margin-bottom: 20px; border-radius: 8px; background-color: #fff; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }");
        out.println("button { padding: 10px 18px; cursor: pointer; background-color: #007bff; color: white; border: none; border-radius: 4px; font-weight: bold; }");
        out.println("button:hover { background-color: #0056b3; }");
        out.println("input { padding: 10px; margin-right: 10px; border: 1px solid #ccc; border-radius: 4px; width: 200px; }");
        out.println("pre { background: #2d2d2d; color: #f8f8f2; padding: 15px; border-radius: 4px; overflow-x: auto; min-height: 50px; margin-top: 15px; white-space: pre-wrap; word-wrap: break-word; }");
        out.println("h2 { margin-top: 0; color: #333; font-size: 1.2rem; border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 15px; }");
        out.println("h3 { margin-top: 0; font-size: 1rem; color: #555; }");
        out.println(".url-display { font-family: monospace; color: #d63384; background: #fdf0f5; padding: 2px 6px; border-radius: 4px; margin-bottom: 10px; display: inline-block; }");
        out.println(".error { color: red; font-weight: bold; }");
        out.println("</style>");
        out.println("</head><body>");

        out.println("<h1>Library API Test Dashboard</h1>");
        out.println("<p>Base URL: <span id='baseUrl' class='url-display'>Loading...</span></p>");

        // -1. Setup (Run this first!)
        out.println("<div class='section' style='background-color: #fff3cd; border-color: #ffeeba;'>");
        out.println("<h2 style='color: #856404;'>⚠️ Setup (Run Once)</h2>");
        out.println("<p>If you see 'Table doesn't exist' errors, run these:</p>");
        out.println("<button onclick=\"apiCall('/init-db', {}, 'initResult')\">1. Initialize Database Tables</button>");
        out.println("<button onclick=\"apiCall('/seed-books', {}, 'initResult')\">2. Add 10 Random Books</button>");
        out.println("<pre id='initResult' style='background: #fff; color: #333; border: 1px solid #ddd;'>Ready...</pre>");
        out.println("</div>");

        // 0. Authentication
        out.println("<div class='section' style='border-left: 5px solid #007bff;'>");
        out.println("<h2>0. Authentication (Login / Signup)</h2>");
        
        out.println("<h3>Login</h3>");
        out.println("<p>POST <span class='url-display' id='url-login'>/api/auth/login</span></p>");
        out.println("<input type='text' id='loginUser' placeholder='Username' value='admin'>");
        out.println("<input type='password' id='loginPass' placeholder='Password' value='admin'>");
        out.println("<button onclick='login()'>Login</button>");
        out.println("<pre id='loginResult'>Ready to test...</pre>");
        
        out.println("<hr style='margin: 20px 0; border: 0; border-top: 1px solid #eee;'>");
        
        out.println("<h3>Signup</h3>");
        out.println("<p>POST <span class='url-display' id='url-signup'>/api/auth/signup</span></p>");
        out.println("<input type='text' id='signUser' placeholder='Username'>");
        out.println("<input type='password' id='signPass' placeholder='Password'>");
        out.println("<input type='email' id='signEmail' placeholder='Email'>");
        out.println("<button onclick='signup()'>Signup</button>");
        out.println("<pre id='signResult'>Ready to test...</pre>");
        out.println("</div>");

        // 0.5 List Users
        out.println("<div class='section'>");
        out.println("<h2>0.5 List All Users</h2>");
        out.println("<p>GET <span class='url-display' id='url-users'>/api/users</span></p>");
        out.println("<button onclick='fetchAllUsers()'>Fetch All Users</button>");
        out.println("<pre id='allUsersResult'>Results will appear here...</pre>");
        out.println("</div>");

        // 1. List All Books
        out.println("<div class='section'>");
        out.println("<h2>1. List All Books</h2>");
        out.println("<p>GET <span class='url-display' id='url-books'>/api/books</span></p>");
        out.println("<button onclick='fetchAllBooks()'>Fetch All Books</button>");
        out.println("<pre id='allBooksResult'>Results will appear here...</pre>");
        out.println("</div>");

        // 2. Search Books
        out.println("<div class='section'>");
        out.println("<h2>2. Search Books</h2>");
        out.println("<p>GET <span class='url-display' id='url-search'>/api/books?query={term}</span></p>");
        out.println("<input type='text' id='searchQuery' placeholder='Search term (e.g. Gatsby)'>");
        out.println("<button onclick='searchBooks()'>Search</button>");
        out.println("<pre id='searchResult'>Results will appear here...</pre>");
        out.println("</div>");

        // 3. Borrow Book
        out.println("<div class='section'>");
        out.println("<h2>3. Borrow Book</h2>");
        out.println("<p>POST <span class='url-display' id='url-borrow'>/api/borrow</span></p>");
        out.println("<input type='number' id='borrowUserId' placeholder='User ID (e.g. 1)'>");
        out.println("<input type='number' id='borrowBookId' placeholder='Book ID (e.g. 1)'>");
        out.println("<button onclick='borrowBook()'>Borrow Book</button>");
        out.println("<pre id='borrowResult'>Results will appear here...</pre>");
        out.println("</div>");

        // 4. User Borrow History
        out.println("<div class='section'>");
        out.println("<h2>4. User Borrow History</h2>");
        out.println("<p>GET <span class='url-display' id='url-history'>/api/borrow?userId={id}</span></p>");
        out.println("<input type='number' id='historyUserId' placeholder='User ID (e.g. 1)'>");
        out.println("<button onclick='fetchHistory()'>Fetch History</button>");
        out.println("<pre id='historyResult'>Results will appear here...</pre>");
        out.println("</div>");

        // 5. Return Book
        out.println("<div class='section'>");
        out.println("<h2>5. Return Book</h2>");
        out.println("<p>PUT <span class='url-display' id='url-return'>/api/borrow</span></p>");
        out.println("<input type='number' id='returnBorrowId' placeholder='Borrow ID (from history)'>");
        out.println("<button onclick='returnBook()'>Return Book</button>");
        out.println("<pre id='returnResult'>Results will appear here...</pre>");
        out.println("</div>");

        // 6. Admin: Manage Books
        out.println("<div class='section' style='border-left: 5px solid #dc3545;'>");
        out.println("<h2>6. Admin: Manage Books</h2>");
        out.println("<p>API: <span class='url-display' id='url-admin-books'>/api/admin/books</span></p>");
        
        out.println("<h3>Create Book</h3>");
        out.println("<input type='text' id='newBookTitle' placeholder='Title'>");
        out.println("<input type='text' id='newBookAuthor' placeholder='Author'>");
        out.println("<input type='text' id='newBookIsbn' placeholder='ISBN'>");
        out.println("<input type='number' id='newBookCopies' placeholder='Copies' value='5'>");
        out.println("<button onclick='createBook()'>Create</button>");

        out.println("<h3>Update Book</h3>");
        out.println("<input type='number' id='updateBookId' placeholder='ID' style='width: 60px;'>");
        out.println("<input type='text' id='updateBookTitle' placeholder='New Title'>");
        out.println("<input type='number' id='updateBookCopies' placeholder='Copies' style='width: 80px;'>");
        out.println("<button onclick='updateBook()'>Update</button>");

        out.println("<h3>Delete Book</h3>");
        out.println("<input type='number' id='deleteBookId' placeholder='Book ID'>");
        out.println("<button onclick='deleteBook()' style='background-color: #dc3545;'>Delete</button>");
        
        out.println("<pre id='adminBookResult'>Admin Book Results...</pre>");
        out.println("</div>");

        // 7. Admin: Manage Users
        out.println("<div class='section' style='border-left: 5px solid #dc3545;'>");
        out.println("<h2>7. Admin: Manage Users</h2>");
        out.println("<p>API: <span class='url-display' id='url-admin-users'>/api/admin/users</span></p>");
        
        out.println("<h3>Update User</h3>");
        out.println("<input type='number' id='updateUserId' placeholder='ID' style='width: 60px;'>");
        out.println("<input type='text' id='updateUserEmail' placeholder='New Email'>");
        out.println("<input type='text' id='updateUserRole' placeholder='Role (USER/ADMIN)'>");
        out.println("<button onclick='updateUser()'>Update</button>");

        out.println("<h3>Delete User</h3>");
        out.println("<input type='number' id='deleteUserId' placeholder='User ID'>");
        out.println("<button onclick='deleteUser()' style='background-color: #dc3545;'>Delete</button>");
        
        out.println("<pre id='adminUserResult'>Admin User Results...</pre>");
        out.println("</div>");

        // 8. Admin: Stats
        out.println("<div class='section' style='border-left: 5px solid #17a2b8;'>");
        out.println("<h2>8. Admin: Dashboard Stats</h2>");
        out.println("<p>API: <span class='url-display' id='url-admin-stats'>/api/admin/stats</span></p>");
        out.println("<button onclick='fetchStats()' style='background-color: #17a2b8;'>Fetch Stats</button>");
        out.println("<pre id='statsResult'>Stats will appear here...</pre>");
        out.println("</div>");

        // Scripts
        out.println("<script>");
        out.println("const baseUrl = window.location.origin;");
        out.println("document.getElementById('baseUrl').innerText = baseUrl;");
        
        // Update URL displays with full paths
        out.println("document.querySelectorAll('.url-display').forEach(el => {");
        out.println("  if(el.id !== 'baseUrl') el.innerText = baseUrl + el.innerText;");
        out.println("});");

        out.println("async function apiCall(url, options = {}, resultElementId) {");
        out.println("  const resultEl = document.getElementById(resultElementId);");
        out.println("  resultEl.innerText = 'Loading...';");
        out.println("  try {");
        out.println("    console.log('Fetching:', url, options);");
        out.println("    const res = await fetch(url, options);");
        out.println("    console.log('Response status:', res.status);");
        out.println("    const contentType = res.headers.get('content-type');");
        out.println("    let data;");
        out.println("    if (contentType && contentType.indexOf('application/json') !== -1) {");
        out.println("      data = await res.json();");
        out.println("    } else {");
        out.println("      const text = await res.text();");
        out.println("      data = { error: 'Non-JSON response', status: res.status, body: text };");
        out.println("    }");
        out.println("    resultEl.innerText = JSON.stringify(data, null, 2);");
        out.println("  } catch (error) {");
        out.println("    console.error('Fetch error:', error);");
        out.println("    resultEl.innerText = 'Error: ' + error.message;");
        out.println("  }");
        out.println("}");

        out.println("function login() {");
        out.println("  const u = document.getElementById('loginUser').value;");
        out.println("  const p = document.getElementById('loginPass').value;");
        out.println("  apiCall('/api/auth/login', {");
        out.println("    method: 'POST',");
        out.println("    headers: {'Content-Type': 'application/json'},");
        out.println("    body: JSON.stringify({username: u, password: p})");
        out.println("  }, 'loginResult');");
        out.println("}");

        out.println("function signup() {");
        out.println("  const u = document.getElementById('signUser').value;");
        out.println("  const p = document.getElementById('signPass').value;");
        out.println("  const e = document.getElementById('signEmail').value;");
        out.println("  apiCall('/api/auth/signup', {");
        out.println("    method: 'POST',");
        out.println("    headers: {'Content-Type': 'application/json'},");
        out.println("    body: JSON.stringify({username: u, password: p, email: e})");
        out.println("  }, 'signResult');");
        out.println("}");

        out.println("function fetchAllUsers() {");
        out.println("  apiCall('/api/users', {}, 'allUsersResult');");
        out.println("}");

        out.println("function fetchAllBooks() {");
        out.println("  apiCall('/api/books', {}, 'allBooksResult');");
        out.println("}");

        out.println("function searchBooks() {");
        out.println("  const query = document.getElementById('searchQuery').value;");
        out.println("  apiCall('/api/books?query=' + encodeURIComponent(query), {}, 'searchResult');");
        out.println("}");

        out.println("function borrowBook() {");
        out.println("  const userId = document.getElementById('borrowUserId').value;");
        out.println("  const bookId = document.getElementById('borrowBookId').value;");
        out.println("  apiCall('/api/borrow', {");
        out.println("    method: 'POST',");
        out.println("    headers: {'Content-Type': 'application/json'},");
        out.println("    body: JSON.stringify({userId: userId, bookId: bookId})");
        out.println("  }, 'borrowResult');");
        out.println("}");

        out.println("function fetchHistory() {");
        out.println("  const userId = document.getElementById('historyUserId').value;");
        out.println("  apiCall('/api/borrow?userId=' + userId, {}, 'historyResult');");
        out.println("}");

        out.println("function returnBook() {");
        out.println("  const borrowId = document.getElementById('returnBorrowId').value;");
        out.println("  apiCall('/api/borrow', {");
        out.println("    method: 'PUT',");
        out.println("    headers: {'Content-Type': 'application/json'},");
        out.println("    body: JSON.stringify({borrowId: borrowId})");
        out.println("  }, 'returnResult');");
        out.println("}");

        // Admin Book Functions
        out.println("function createBook() {");
        out.println("  const title = document.getElementById('newBookTitle').value;");
        out.println("  const author = document.getElementById('newBookAuthor').value;");
        out.println("  const isbn = document.getElementById('newBookIsbn').value;");
        out.println("  const copies = document.getElementById('newBookCopies').value;");
        out.println("  apiCall('/api/admin/books', {");
        out.println("    method: 'POST',");
        out.println("    headers: {'Content-Type': 'application/json'},");
        out.println("    body: JSON.stringify({title, author, isbn, availableCopies: parseInt(copies)})");
        out.println("  }, 'adminBookResult');");
        out.println("}");

        out.println("function updateBook() {");
        out.println("  const id = document.getElementById('updateBookId').value;");
        out.println("  const title = document.getElementById('updateBookTitle').value;");
        out.println("  const copies = document.getElementById('updateBookCopies').value;");
        out.println("  const body = { id: parseInt(id) };");
        out.println("  if(title) body.title = title;");
        out.println("  if(copies) body.availableCopies = parseInt(copies);");
        out.println("  body.author = 'Updated Author';");
        out.println("  body.isbn = 'Updated-ISBN-' + id;");
        out.println("  apiCall('/api/admin/books', {");
        out.println("    method: 'PUT',");
        out.println("    headers: {'Content-Type': 'application/json'},");
        out.println("    body: JSON.stringify(body)");
        out.println("  }, 'adminBookResult');");
        out.println("}");

        out.println("function deleteBook() {");
        out.println("  const id = document.getElementById('deleteBookId').value;");
        out.println("  apiCall('/api/admin/books?id=' + id, { method: 'DELETE' }, 'adminBookResult');");
        out.println("}");

        // Admin User Functions
        out.println("function updateUser() {");
        out.println("  const id = document.getElementById('updateUserId').value;");
        out.println("  const email = document.getElementById('updateUserEmail').value;");
        out.println("  const role = document.getElementById('updateUserRole').value;");
        out.println("  const body = { id: parseInt(id) };");
        out.println("  body.username = 'user' + id;");
        out.println("  if(email) body.email = email;");
        out.println("  if(role) body.role = role;");
        out.println("  apiCall('/api/admin/users', {");
        out.println("    method: 'PUT',");
        out.println("    headers: {'Content-Type': 'application/json'},");
        out.println("    body: JSON.stringify(body)");
        out.println("  }, 'adminUserResult');");
        out.println("}");

        out.println("function deleteUser() {");
        out.println("  const id = document.getElementById('deleteUserId').value;");
        out.println("  apiCall('/api/admin/users?id=' + id, { method: 'DELETE' }, 'adminUserResult');");
        out.println("}");

        out.println("function fetchStats() {");
        out.println("  apiCall('/api/admin/stats', { method: 'GET' }, 'statsResult');");
        out.println("}");
        out.println("</script>");

        out.println("</body></html>");
    }
}