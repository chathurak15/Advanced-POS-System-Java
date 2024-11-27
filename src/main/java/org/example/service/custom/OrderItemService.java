package org.example.service.custom;

import org.example.repo.custom.OrderItemRepo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemService {

    private final OrderItemRepo orderItemRepo = new OrderItemRepo();

    public List<List<Integer>> getAllOrderItems() throws Exception {
        try {
            List<String> productIdsList = orderItemRepo.getProductIdsByOrder();

            List<List<Integer>> allOrderItems = new ArrayList<>();

            for (String productIds : productIdsList) {
                String[] productIdArray = productIds.split(",");
                List<Integer> productList = new ArrayList<>();
                for (String productId : productIdArray) {
                    productList.add(Integer.parseInt(productId));
                }
                allOrderItems.add(productList);
            }return allOrderItems;
        }catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
