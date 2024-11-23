package org.example.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProductDTO {
    private int id;
    private String name;
    private String category;
    private double price;
    private double cost;
    private int quantity;
    private int supplierid;
    private String expirydate;
    private String date;
}
