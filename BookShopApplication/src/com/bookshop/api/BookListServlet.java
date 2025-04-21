package com.bookshop.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookshop.model.Book;
import com.bookshop.service.BookService;

@WebServlet("/api/booklist")
public class BookListServlet extends HttpServlet {
    private BookService b = new BookService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        List<Book> bl = b.getallBooks();
        req.setAttribute("BookList", bl);
        req.getRequestDispatcher("/jsp/booklist.jsp").forward(req, res);
    }
}
