package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderDTO {
    private int orderId;
    private String customerNumber;
    private double totalAmount;
    private double totalDiscount;
    private int totalItems;
    private String billDate;
//    private List<OrderItem> orderItems;
}
