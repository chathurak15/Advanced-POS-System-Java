package org.example.repo.custom;

import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.util.DBConnection;
import java.sql.*;


public class OrderRepo {

    public Integer addOrder(Order order) throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();

        // Prepare statement with RETURN_GENERATED_KEYS
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO orders (customer_phone, totalAmount, totalDiscount, totalItems,billDate) VALUES (?, ?, ?, ?,?)",
                Statement.RETURN_GENERATED_KEYS);

        // Set values for placeholders
        ps.setDouble(2, order.getTotalAmount());
        ps.setDouble(3, order.getTotalDiscount());
        ps.setInt(4, order.getTotalItems());
        ps.setString(5, order.getBillDate());

        if(order.getCustomerNumber()==null || order.getCustomerNumber().equals("+94")){
            ps.setNull(1, java.sql.Types.VARCHAR);
        }else {
            ps.setString(1, order.getCustomerNumber());
        }


        // Execute update and check affected rows
        int affectedRows = ps.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating order failed, no rows affected.");
        }

        // Retrieve generated keys
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                order.setOrderId((int) generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
        }

        return order.getOrderId();
    }


    public boolean AddOrderItem(OrderItem orderItem) throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO order_item (product_id,Order_id,quantity,sellPrice) VALUES (?,?,?,?)");

        ps.setInt(1, orderItem.getProductId());
        ps.setInt(2, orderItem.getOrderId());
        ps.setInt(3, orderItem.getQuantity());
        ps.setDouble(4, orderItem.getSellPrice());

        return ps.executeUpdate()>0;
    }
}
