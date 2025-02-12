package com.jaro.webnookbook.servlets;

import com.jaro.webnookbook.managers.CustomerManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/AddFundsServlet")
public class AddFundsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String userLogin = (String) session.getAttribute("userLogin");

        if (userLogin == null) {
            response.sendRedirect("login.jsp?error=Please log in first");
            return;
        }

        String amountParam = request.getParameter("amount");
        if (amountParam == null || amountParam.isEmpty()) {
            response.sendRedirect("customerDashboard.jsp?error=Invalid amount");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountParam);
            if (amount <= 0) {
                response.sendRedirect("customerDashboard.jsp?error=Amount must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("customerDashboard.jsp?error=Invalid amount format");
            return;
        }

        boolean success = CustomerManager.addFunds(userLogin, amount);
        if (success) {
            response.sendRedirect("customerDashboard.jsp?success=Funds added successfully");
        } else {
            response.sendRedirect("customerDashboard.jsp?error=Failed to add funds");
        }
    }
}
