package org.example.tm;

import lombok.Data;

@Data
public class LowStockTM {
    private int id;
    private String name;
    private String supplierName;
    private String supplierEmail;
    private int quantity;
}
