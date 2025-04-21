package com.bookshop.api;

import com.bookshop.model.Order;
import com.bookshop.model.OrderItem;
import com.bookshop.model.User;
import com.bookshop.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/orderhistory")
public class OrderHistoryServlet extends HttpServlet {

    private OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            res.sendRedirect(req.getContextPath() + "/jsp/LoginOrRegister.jsp");
            return;
        }

        User currentUser = (User) session.getAttribute("currentUser");

        try {
            List<Order> orders = orderService.getOrdersByUserId(currentUser.getUserId());

            if (orders != null && !orders.isEmpty()) {
                // Populate each order with its order items
                for (Order order : orders) {
                    List<OrderItem> orderItems = orderService.getOrderItemsByOrderId(order.getOrderId());
                    order.setItems(orderItems);
                }
                req.setAttribute("orderHistory", orders);
            } else {
                req.setAttribute("message", "You haven't placed any orders yet.");
            }

            req.getRequestDispatcher("/jsp/orderhistory.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "An error occurred while fetching your order history. Please try again later.");
            req.getRequestDispatcher("/jsp/orderhistory.jsp").forward(req, res);
        }
    }
}

