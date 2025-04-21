package com.bookshop.api;

import com.bookshop.model.Order;
import com.bookshop.model.OrderItem;
import com.bookshop.model.User;
import com.bookshop.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/api/placeorder")
public class PlaceOrderServlet extends HttpServlet {

    private OrderService orderService = new OrderService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/loginOrRegister.jsp");
            return;
        }

        try {
            User currentUser = (User) session.getAttribute("currentUser");
            int userId = currentUser.getUserId();

            // Get book details from the form
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String priceStr = request.getParameter("price");

            if (priceStr == null || priceStr.isEmpty()) {
                response.getWriter().println("Price is missing.");
                return;
            }

            BigDecimal price;
            try {
                price = new BigDecimal(priceStr);
            } catch (NumberFormatException e) {
                response.getWriter().println("Invalid price format. Please provide a valid number.");
                return;
            }

            // Calculate total for this single item order
            BigDecimal totalAmount = price.multiply(new BigDecimal(quantity));

            // Create and save the order
            Order order = new Order();
            order.setUserId(userId);
            order.setTotalAmount(totalAmount);

            int generatedOrderId = orderService.saveOrder(order);

            if (generatedOrderId > 0) {
                // Prepare and save order item
                OrderItem item = new OrderItem();
                item.setOrderId(generatedOrderId);
                item.setBookId(bookId);
                item.setQuantity(quantity);
                item.setPrice(price);

                boolean itemSaved = orderService.saveOrderItem(item);

                if (itemSaved) {
                    response.sendRedirect(request.getContextPath() + "/jsp/orderconfirmation.jsp");
                } else {
                    response.getWriter().println("Order placed but failed to save items.");
                }

            } else {
                response.getWriter().println("Failed to place the order. Try again!");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.getWriter().println("Invalid input! Make sure all fields are correct.");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error occurred while placing the order.");
        }
    }
}
