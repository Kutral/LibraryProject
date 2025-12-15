<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login - The Knowledge Nexus</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Login</h1>
        <p>Welcome back.</p>
        
        <%
            String param = request.getParameter("signupSuccess");
            if ("true".equals(param)) {
        %>
            <div class="success-message">Registration successful! Please login.</div>
        <%
            }
        %>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn">Login</button>
        </form>
        
        <p class="footer-text">
            Don't have an account? <a href="signup.jsp" class="link-highlight">Sign up here</a>.
        </p>
    </div>
</body>
</html>