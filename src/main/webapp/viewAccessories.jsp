<%@page import="com.jaro.webnookbook.managers.AccessoryManager"%>
<%@page import="com.jaro.webnookbook.models.Accessory"%>
<%@ page import="java.util.ArrayList" %>

<%@ page session="true" %>
<html>
    <head><title>Accessory List</title></head>
    <body>
        <h2>Available Accessories</h2>
        <%
            ArrayList<Accessory> accessories = AccessoryManager.getAllAccessories();
            for (Accessory accessory : accessories) {
        %>
        <p>
            <b>Name:</b> <%= accessory.getName()%><br>
            <b>Price:</b> $<%= accessory.getPrice()%><br>
        </p>
        <hr>
        <% }%>
        <a href="customerDashboard.jsp">Back</a>
    </body>
</html>
