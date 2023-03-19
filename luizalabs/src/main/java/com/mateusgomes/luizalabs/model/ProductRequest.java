package com.mateusgomes.luizalabs.model;

import java.util.UUID;

public class ProductRequest {

    private UUID idProduct;

    public ProductRequest() {
    }

    public UUID getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(UUID idProduct) {
        this.idProduct = idProduct;
    }
}
