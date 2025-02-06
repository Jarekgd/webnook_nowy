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
            response.sendRedirect("login.jsp?error=Please log in to add funds");
            return;
        }

        String amountStr = request.getParameter("amount");
        
        if (amountStr == null || amountStr.isEmpty()) {
            response.sendRedirect("customerDashboard.jsp?error=Invalid amount");
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                response.sendRedirect("customerDashboard.jsp?error=Amount must be greater than zero");
                return;
            }
            
            double currentBalance = CustomerManager.getBalance(userLogin);
            double newBalance = currentBalance + amount;
            CustomerManager.updateBalance(userLogin, newBalance);
            
            response.sendRedirect("customerDashboard.jsp?success=Funds added successfully");
        } catch (NumberFormatException e) {
            response.sendRedirect("customerDashboard.jsp?error=Invalid amount format");
        }
    }
}
