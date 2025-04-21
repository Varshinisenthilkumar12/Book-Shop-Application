package com.bookshop.service;
import com.bookshop.model.*;
import java.sql.*;

public class UserService {
	private static final String URL="jdbc:mysql://localhost:3306/bookshopapp";
	private static final String USERNAME="root";
	private static final String PASSWORD="Varsh@12";
	static {
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        System.out.println("Driver Loaded Successfully!");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	
	public boolean registerUser(String name,String email,String password,String role) {
		

		if(getUserByEmail(email)!=null) {
			return false;
		}
		try(Connection con = DriverManager.getConnection(URL,USERNAME,PASSWORD)){
			String sql="INSERT INTO users(name,email,password,role) values (?,?,?,?)";
			try(PreparedStatement stmt = con.prepareStatement(sql)){
				stmt.setString(1,name);
				stmt.setString(2,email);
				stmt.setString(3,password);
				stmt.setString(4,role);
				stmt.executeUpdate();
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean loginUser(String email,String password) {
		User u=getUserByEmail(email);
		if(u==null) {
			return false;
		}
		return u.getPassword().equals(password);
	}
	public User getUserByEmail(String email) {
		User u=null;
		try(Connection con = DriverManager.getConnection(URL,USERNAME,PASSWORD)){
			String sql="Select * from users where email=?";
			try(PreparedStatement stmt = con.prepareStatement(sql)){
				stmt.setString(1,email);
				ResultSet rs=stmt.executeQuery();
				if(rs.next()) {
					u=new User(
						rs.getInt("user_id"),
						rs.getString("name"),
						rs.getString("email"),
						rs.getString("password"),
						rs.getString("role")
					);
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return u;
	}
}
