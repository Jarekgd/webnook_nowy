package com.jaro.webnookbook.servlets;

import com.jaro.webnookbook.models.Book;
import com.jaro.webnookbook.managers.BookManager;
import com.jaro.webnookbook.managers.CartManager;
import com.jaro.webnookbook.managers.AccessoryManager;
import com.jaro.webnookbook.models.Accessory;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AddToCartServlet")
public class AddToCartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String userLogin = (String) session.getAttribute("userLogin");

        if (userLogin == null) {
            System.out.println("DEBUG: User is not logged in.");
            response.sendRedirect("login.jsp?error=Please log in to add items to your cart");
            return;
        }

        // Retrieve form parameters
        String serialNo = request.getParameter("serialNo");
        String productType = request.getParameter("productType");
        String quantityParam = request.getParameter("quantity");

        System.out.println("DEBUG: UserLogin: " + userLogin);
        System.out.println("DEBUG: SerialNo: " + serialNo);
        System.out.println("DEBUG: ProductType: " + productType);
        System.out.println("DEBUG: Quantity (String): " + quantityParam);

        if (serialNo == null || serialNo.isEmpty() || productType == null || productType.isEmpty() || quantityParam == null || quantityParam.isEmpty()) {
            System.out.println("DEBUG: Invalid item or quantity.");
            response.sendRedirect("customerCart.jsp?error=Invalid item or quantity");

            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityParam);
            if (quantity <= 0) {
                System.out.println("DEBUG: Invalid quantity (must be > 0)");
                response.sendRedirect("customerDashboard.jsp?error=Quantity must be at least 1");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("DEBUG: Invalid quantity format.");
            response.sendRedirect("customerDashboard.jsp?error=Invalid quantity format");
            return;
        }

        try {
            // Retrieve product details
            String productName = "";
            double productPrice = 0.0;

            if ("book".equalsIgnoreCase(productType)) {
                Book book = BookManager.getBookBySerialNo(serialNo);
                if (book != null) {
                    productName = book.getName(); // Using getName() from Product class
                    productPrice = book.getPrice();
                    System.out.println("DEBUG: Book Found - " + productName + " | Price: " + productPrice);
                } else {
                    System.out.println("DEBUG: Book not found for SerialNo: " + serialNo);
                }
            } else if ("accessory".equalsIgnoreCase(productType)) {
                Accessory accessory = AccessoryManager.getAccessoryBySerialNo(serialNo);
                if (accessory != null) {
                    productName = accessory.getName(); // Using getName() from Product class
                    productPrice = accessory.getPrice();
                    System.out.println("DEBUG: Accessory Found - " + productName + " | Price: " + productPrice);
                } else {
                    System.out.println("DEBUG: Accessory not found for SerialNo: " + serialNo);
                }
            }

            if (!productName.isEmpty()) {
                CartManager.addToCart(userLogin, serialNo, productName, productPrice, quantity);
                response.sendRedirect("customerCart.jsp?success=Item added to cart");
            } else {
                response.sendRedirect("customerDashboard.jsp?error=Item not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("customerDashboard.jsp?error=Database error");
        }
    }
}
