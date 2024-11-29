package org.example.dto.AI;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RelationshipOfferDTO {
    private int id;
    private String mainProduct;
    private String freeProduct;
    private String status;
}
