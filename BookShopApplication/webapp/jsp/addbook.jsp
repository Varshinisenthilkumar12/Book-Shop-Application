<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.bookshop.model.Book" %>
<%@ page import="com.bookshop.service.BookService" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Book</title>
</head>
<body>
    <h2>Add Book</h2>
    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
        <p style="color:red;"><%= error %></p>
    <% } %>
    <form action="${pageContext.request.contextPath}/api/addbook" method="post">
        Title: <input type="text" name="title" required/><br><br>
        Author: <input type="text" name="author" required/><br><br>
        Price: <input type="number" name="price" step="0.01" required/><br><br>
        Quantity: <input type="number" name="quantity" required/><br><br>
        Category: <input type="text" name="category" /><br><br>
        Description:<br>
        <textarea name="description"></textarea><br><br>
        <button type="submit">Add Book</button>
    </form>
    <% 
    String role = (session.getAttribute("currentUser") != null) 
                    ? ((com.bookshop.model.User)session.getAttribute("currentUser")).getRole() 
                    : null;

    if ("admin".equalsIgnoreCase(role)) {
%>
    <br><a href="<%= request.getContextPath() %>/api/booklist">View Book List</a>
<% 
    }
%>
</body>
</html>
