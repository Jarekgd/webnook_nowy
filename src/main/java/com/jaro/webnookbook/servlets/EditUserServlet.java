package com.jaro.webnookbook.servlets;

import com.jaro.webnookbook.models.User;
import com.jaro.webnookbook.managers.UserManager;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/EditUserServlet")
public class EditUserServlet extends HttpServlet {

    // Load user details for editing (GET)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String loginParam = request.getParameter("id");

            // Validate login parameter
            if (loginParam == null || loginParam.trim().isEmpty()) {
                response.sendRedirect("manageUsers.jsp?error=Invalid user login");
                return;
            }

            // Fetch user from database
            User user = UserManager.getUserByLogin(loginParam);

            if (user == null) {
                response.sendRedirect("manageUsers.jsp?error=User not found");
                return;
            }

            // Set user as attribute and forward to editUser.jsp
            request.setAttribute("user", user);
            request.getRequestDispatcher("editUser.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manageUsers.jsp?error=Server error");
        }
    }

    // Save user changes (POST)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String login = request.getParameter("originalLogin");  // Hidden login field
            String email = request.getParameter("email");
            String userName = request.getParameter("userName");
            String newPassword = request.getParameter("password"); // Can be empty
            String privilege = request.getParameter("privilege");

            if (login == null || login.trim().isEmpty()) {
                response.sendRedirect("manageUsers.jsp?error=Invalid user login");
                return;
            }

            // Fetch existing user
            User existingUser = UserManager.getUserByLogin(login);
            if (existingUser == null) {
                response.sendRedirect("manageUsers.jsp?error=User not found");
                return;
            }

            // If password is empty, keep the old password
            String finalPassword = (newPassword == null || newPassword.trim().isEmpty()) ? existingUser.getPassword() : newPassword;

            // Update user
            boolean success = UserManager.updateUser(login, email, userName, finalPassword, privilege);

            if (success) {
                response.sendRedirect("manageUsers.jsp?success=User updated successfully");
            } else {
                response.sendRedirect("manageUsers.jsp?error=Failed to update user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manageUsers.jsp?error=Server error");
        }
    }
}
