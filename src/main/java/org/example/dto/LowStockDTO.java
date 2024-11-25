package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LowStockDTO {
    private int productId;
    private String productName;
    private String SupplierName;
    private String SupplierEmail;
    private int quantity;
}
