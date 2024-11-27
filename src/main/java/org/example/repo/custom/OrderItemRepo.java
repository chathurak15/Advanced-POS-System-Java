package org.example.repo.custom;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OrderItemRepo {

    public List<String> getProductIdsByOrder() throws Exception{
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT GROUP_CONCAT(order_item.product_id ORDER BY order_item.product_id) AS product_ids " +
                        "FROM order_item " +
                        "JOIN orders ON orders.id = order_item.order_id " +
                        "GROUP BY order_item.order_id;"
        );

        ResultSet rs = ps.executeQuery();

        List<String> productIdsList = new ArrayList<>();
        while (rs.next()) {
            productIdsList.add(rs.getString("product_ids"));
        }

        rs.close();
        ps.close();
        return productIdsList;
    }
}
