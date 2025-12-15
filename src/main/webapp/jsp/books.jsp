<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    if (request.getAttribute("books") == null) {
        response.sendRedirect(request.getContextPath() + "/books");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Library - Book List</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container container-lg">
        <h1>Library Books</h1>
        <p>Browse our collection.</p>
        
        <div class="mb-20">
             <!-- Conditionally show 'Add Book' only if admin -->
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/jsp/addBook.jsp" class="btn">Add New Book</a>
            </c:if>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>ISBN</th>
                    <th>Available</th>
                    <c:if test="${sessionScope.user.role == 'ADMIN'}">
                        <th>Actions</th>
                    </c:if>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="book" items="${books}">
                    <tr>
                        <td>${book.id}</td>
                        <td>${book.title}</td>
                        <td>${book.author}</td>
                        <td>${book.isbn}</td>
                        <td>${book.availableCopies}</td>
                        <c:if test="${sessionScope.user.role == 'ADMIN'}">
                            <td>
                                <a href="#" class="action-btn btn-edit">Edit</a>
                                <a href="#" class="action-btn btn-delete">Delete</a>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="mt-20">
            <c:choose>
                <c:when test="${sessionScope.user.role == 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/jsp/adminDashboard.jsp" class="btn btn-secondary">Back to Dashboard</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/jsp/dashboard.jsp" class="btn btn-secondary">Back to Dashboard</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>