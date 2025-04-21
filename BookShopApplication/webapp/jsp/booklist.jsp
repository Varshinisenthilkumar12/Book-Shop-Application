<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.bookshop.model.Book" %>
<%@ page import="com.bookshop.model.User" %>
<%@ page import="com.bookshop.model.Order" %>
<%@ page import="com.bookshop.service.OrderService" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book List</title>
    <style>
        .edit-form { display: none; }
        .order-history { margin-top: 30px; border-top: 2px solid #ccc; padding-top: 20px; }
        .order-history table { width: 100%; border-collapse: collapse; }
        .order-history th, .order-history td { padding: 8px 12px; border: 1px solid #ddd; text-align: left; }
    </style>
    <script>
        function toggleEditForm(bookId) {
            var form = document.getElementById("edit-form-" + bookId);
            form.style.display = (form.style.display === "none") ? "block" : "none";
        }
    </script>
</head>
<body>

<h2>Available Books</h2>

<%
    List<Book> bookList = (List<Book>) request.getAttribute("BookList");
    User currentUser = (User) session.getAttribute("currentUser");
    String role = (currentUser != null) ? currentUser.getRole() : null;

    if (bookList != null && !bookList.isEmpty()) {
%>
<table border="1" cellpadding="8">
    <tr>
        <th>ID</th><th>Title</th><th>Author</th><th>Price</th>
        <th>Quantity</th><th>Category</th><th>Description</th>
<% if ("admin".equalsIgnoreCase(role) || "customer".equalsIgnoreCase(role)) { %>
        <th>Actions</th>
<% } %>
    </tr>

<% for (Book book : bookList) { %>
    <tr>
        <td><%= book.getBookId() %></td>
        <td><%= book.getTitle() %></td>
        <td><%= book.getAuthor() %></td>
        <td><%= book.getPrice() %></td>
        <td><%= book.getQuantity() %></td>
        <td><%= book.getCategory() %></td>
        <td><%= book.getDescription() %></td>

<% if ("admin".equalsIgnoreCase(role)) { %>
        <td>
            <button onclick="toggleEditForm(<%= book.getBookId() %>)">Edit</button> |
            <a href="<%= request.getContextPath() %>/api/addbook?action=delete&id=<%= book.getBookId() %>" 
               onclick="return confirm('Are you sure you want to delete this book?');">Delete</a>
        </td>
<% } else if ("customer".equalsIgnoreCase(role)) { %>
        <td>
            <form action="<%= request.getContextPath() %>/api/placeorder" method="post" style="display:inline;">
                <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
                <input type="hidden" name="price" value="<%= book.getPrice() %>">
                <input type="number" name="quantity" value="1" min="1" max="<%= book.getQuantity() %>" required>
                <button type="submit">Place Order</button>
            </form>
        </td>
<% } %>
    </tr>

    <!-- Hidden Edit Form for Admin -->
<% if ("admin".equalsIgnoreCase(role)) { %>
    <tr id="edit-form-<%= book.getBookId() %>" class="edit-form">
        <td colspan="8">
            <form action="<%= request.getContextPath() %>/api/addbook" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="<%= book.getBookId() %>">
                <label>Title:</label>
                <input type="text" name="title" value="<%= book.getTitle() %>" required><br>
                <label>Author:</label>
                <input type="text" name="author" value="<%= book.getAuthor() %>" required><br>
                <label>Price:</label>
                <input type="number" name="price" value="<%= book.getPrice() %>" step="0.01" required><br>
                <label>Quantity:</label>
                <input type="number" name="quantity" value="<%= book.getQuantity() %>" required><br>
                <label>Category:</label>
                <input type="text" name="category" value="<%= book.getCategory() %>" required><br>
                <label>Description:</label>
                <textarea name="description" required><%= book.getDescription() %></textarea><br>
                <button type="submit">Update Book</button>
            </form>
        </td>
    </tr>
<% } %>

<% } %>
</table>

<% } else { %>
    <p>No books available.</p>
<% } %>

<% if ("admin".equalsIgnoreCase(role)) { %>
    <br><a href="<%= request.getContextPath() %>/jsp/addbook.jsp">Add New Book</a>
<% } %>

<% 
// Display link to order history for customers
if ("customer".equalsIgnoreCase(role)) {
%>
    <br><a href="<%= request.getContextPath() %>/jsp/orderhistory.jsp">View Your Order History</a>
<% 
}
%>

</body>
</html>
