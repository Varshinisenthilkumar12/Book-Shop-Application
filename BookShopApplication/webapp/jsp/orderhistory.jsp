<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.bookshop.model.Order" %>
<%@ page import="com.bookshop.model.OrderItem" %>
<%@ page import="com.bookshop.model.User" %>
<%@ page import="com.bookshop.service.OrderService" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Confirmed</title>
    <style>
        .order-history { margin-top: 30px; border-top: 2px solid #ccc; padding-top: 20px; }
        .order-history table { width: 100%; border-collapse: collapse; }
        .order-history th, .order-history td { padding: 8px 12px; border: 1px solid #ddd; text-align: left; }
    </style>
</head>
<body>

    <% 
    // Display order history for customers
    User currentUser = (User) session.getAttribute("currentUser");
    String role = (currentUser != null) ? currentUser.getRole() : null;

    if ("customer".equalsIgnoreCase(role)) {
        OrderService orderService = new OrderService();
        List<Order> orders = orderService.getOrdersByUserId(currentUser.getUserId());

        if (orders != null && !orders.isEmpty()) {
    %>
    <div class="order-history">
        <h3>Your Order History</h3>
        <table>
            <tr>
                <th>Order ID</th>
                <th>Order Date</th>
                <th>Total Amount</th>
                <th>Order Items</th>
            </tr>
    <% 
            for (Order order : orders) { 
                // Fetching order items for the current order
                List<OrderItem> orderItems = orderService.getOrderItemsByOrderId(order.getOrderId());
    %>
            <tr>
                <td><%= order.getOrderId() %></td>
                <td><%= order.getOrderDate() %></td>
                <td>₹<%= order.getTotalAmount() %></td>
                <td>
                    <ul>
    <% 
                if (orderItems != null && !orderItems.isEmpty()) {
                    for (OrderItem item : orderItems) {
    %>
                        <li>Book ID: <%= item.getBookId() %> | Quantity: <%= item.getQuantity() %> | Price: ₹<%= item.getPrice() %></li>
    <% 
                    }
                } else {
    %>
                    <li>No items found for this order.</li>
    <% 
                }
    %>
                    </ul>
                </td>
            </tr>
    <% 
            } 
    %>
        </table>
    </div>
    <% 
        } else {
    %>
    <div class="order-history">
        <h3>You haven't placed any orders yet.</h3>
    </div>
    <% 
        }
    }
    %>

</body>
</html>
