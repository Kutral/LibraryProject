<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <h1>Admin Dashboard</h1>
    <p>Welcome, Admin.</p>
    <a href="${pageContext.request.contextPath}/admin/users" class="btn">Manage Users</a>
    <a href="${pageContext.request.contextPath}/books" class="btn">Manage Books</a>
    <br><br>
    <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary">Logout</a>
</div>
</body>
</html>
