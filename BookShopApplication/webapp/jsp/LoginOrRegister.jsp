<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login or Register</title>
</head>
<body>
	<div id="loginForm">
		<h2>Login</h2>
		<form action="${pageContext.request.contextPath }/api/user" method="post">
			<input type="hidden" name="action" value="login">
			Email:<input type="email" name="email" required><br><br>
			Password:<input type="password" name="password" required><br><br>
			<button type="submit">Login</button>
		</form>
		<br>
		<a href="#" onclick="showRegisterForm()">Don't have an account? Register</a>
	</div>
	
	<div id="registerForm" style="display:none;">
		<h2>Register</h2>
		<form action="${pageContext.request.contextPath}/api/user" method="post">
			<input type="hidden" name="action" value="register">

            Name: <input type="text" name="name" required><br><br>
            Email: <input type="email" name="email" required><br><br>
            Password: <input type="password" name="password" required><br><br>
            Role:
            <select name="role" required>
                <option value="customer">Customer</option>
                <option value="admin">Admin</option>
            </select><br><br>

            <button type="submit">Register</button>
		</form>
		<br>
        <a href="#" onclick="showLoginForm()">Already have an account? Login</a>
	</div>
	<script>
        function showRegisterForm() {
            document.getElementById('loginForm').style.display = 'none';
            document.getElementById('registerForm').style.display = 'block';
        }
        function showLoginForm() {
            document.getElementById('loginForm').style.display = 'block';
            document.getElementById('registerForm').style.display = 'none';
        }
    </script>
</body>
</html>