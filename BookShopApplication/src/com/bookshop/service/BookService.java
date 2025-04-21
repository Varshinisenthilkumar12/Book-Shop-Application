package com.bookshop.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.bookshop.model.Book;

public class BookService {
    private static final String URL = "jdbc:mysql://localhost:3306/bookshopapp";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Varsh@12";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("BookService: JDBC Driver Loaded!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add Book Method
    public boolean addBook(Book b) {
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO books(title, author, price, quantity, category, description) VALUES(?,?,?,?,?,?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, b.getTitle());
                stmt.setString(2, b.getAuthor());
                stmt.setDouble(3, b.getPrice());
                stmt.setInt(4, b.getQuantity());
                stmt.setString(5, b.getCategory());
                stmt.setString(6, b.getDescription());
                int rows = stmt.executeUpdate();
                System.out.println("Rows affected: " + rows);  // Debugging
                return rows > 0;
                //return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get All Books Method
    public List<Book> getallBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM books");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getString("description")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Books retrieved: " + books.size());  // Debugging
        return books;
    }
 // Update Book Method
    public boolean updateBook(Book b) {
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE books SET title=?, author=?, price=?, quantity=?, category=?, description=? WHERE book_id=?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, b.getTitle());
                stmt.setString(2, b.getAuthor());
                stmt.setDouble(3, b.getPrice());
                stmt.setInt(4, b.getQuantity());
                stmt.setString(5, b.getCategory());
                stmt.setString(6, b.getDescription());
                stmt.setInt(7, b.getBookId());  // WHERE condition
                int rows = stmt.executeUpdate();
                System.out.println("Updated rows: " + rows);  // Debugging
                return rows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
 // Delete Book Method
    public boolean deleteBook(int bookId) {
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM books WHERE book_id=?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, bookId);
                int rows = stmt.executeUpdate();
                System.out.println("Deleted rows: " + rows);  // Debugging
                return rows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
