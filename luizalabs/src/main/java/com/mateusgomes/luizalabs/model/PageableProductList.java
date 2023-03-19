package com.mateusgomes.luizalabs.model;

import java.util.List;

public class PageableProductList {

    private Meta meta;

    private List<Product> products;

    public PageableProductList(Meta meta, List<Product> products) {
        this.meta = meta;
        this.products = products;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
