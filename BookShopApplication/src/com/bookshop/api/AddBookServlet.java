package com.bookshop.api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.bookshop.model.Book;
import com.bookshop.service.BookService;

@WebServlet("/api/addbook")
public class AddBookServlet extends HttpServlet {
    private BookService bookService = new BookService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("update".equalsIgnoreCase(action)) {
            handleUpdate(req, res);
        } else {
            handleAdd(req, res);  // Default behavior: Add Book
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("delete".equalsIgnoreCase(action)) {
            handleDelete(req, res);
        } else {
            res.sendRedirect(req.getContextPath() + "/api/booklist");
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String title = req.getParameter("title");
        String author = req.getParameter("author");
        double price = Double.parseDouble(req.getParameter("price"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        String category = req.getParameter("category");
        String description = req.getParameter("description");

        Book bk = new Book(0, title, author, price, quantity, category, description);
        boolean status = bookService.addBook(bk);

        if (status) {
            res.sendRedirect(req.getContextPath() + "/api/booklist");
        } else {
            req.setAttribute("error", "Failed to add book.");
            req.getRequestDispatcher("/jsp/addbook.jsp").forward(req, res);
        }
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int bookId = Integer.parseInt(req.getParameter("id"));
        String title = req.getParameter("title");
        String author = req.getParameter("author");
        double price = Double.parseDouble(req.getParameter("price"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        String category = req.getParameter("category");
        String description = req.getParameter("description");

        Book updatedBook = new Book(bookId, title, author, price, quantity, category, description);
        boolean status = bookService.updateBook(updatedBook);

        if (status) {
            res.sendRedirect(req.getContextPath() + "/api/booklist");
        } else {
            req.setAttribute("error", "Failed to update book.");
            req.getRequestDispatcher("/jsp/updatebook.jsp").forward(req, res);
        }
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int bookId = Integer.parseInt(req.getParameter("id"));
        boolean status = bookService.deleteBook(bookId);

        if (status) {
            res.sendRedirect(req.getContextPath() + "/api/booklist");
        } else {
            res.getWriter().write("Failed to delete book.");
        }
    }
}
