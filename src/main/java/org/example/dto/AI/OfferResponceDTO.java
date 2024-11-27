package org.example.dto.AI;

import org.example.entity.AI.Item;

public class OfferResponceDTO {
    private String message;
    private Item item;

    public OfferResponceDTO(String message, Item item) {
        this.message = message;
        this.item = item;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
