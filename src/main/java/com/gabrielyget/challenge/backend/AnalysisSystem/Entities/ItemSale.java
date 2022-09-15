package com.gabrielyget.challenge.backend.AnalysisSystem.Entities;

import lombok.Data;

public @Data class ItemSale {
    private String itemId;
    private int quantity;
    private Double price;

    public ItemSale(String itemId, int quantity, Double price) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
    }

}
