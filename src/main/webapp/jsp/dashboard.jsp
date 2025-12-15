<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <h1>User Dashboard</h1>
    <p>Welcome, ${sessionScope.user.username}!</p>
    <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary">Logout</a>
</div>
</body>
</html>
