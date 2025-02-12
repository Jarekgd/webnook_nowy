package com.jaro.webnookbook.servlets;

import com.jaro.webnookbook.managers.UserManager;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {

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

            boolean success = UserManager.deleteUserByLogin(loginParam);

            if (success) {
                response.sendRedirect("manageUsers.jsp?success=User deleted");
            } else {
                response.sendRedirect("manageUsers.jsp?error=User not found or could not be deleted");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manageUsers.jsp?error=Server error");
        }
    }
}
