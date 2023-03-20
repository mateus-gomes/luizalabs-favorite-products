package com.mateusgomes.luizalabs.data.domain;

import java.util.UUID;

public class ProductAPIResponse {

    private String title;

    private Double price;

    private String brand;

    private UUID id;

    private String image;

    private Double reviewScore;

    public ProductAPIResponse() {
    }

    public ProductAPIResponse(String title, Double price, String brand, UUID id, String image, Double reviewScore) {
        this.title = title;
        this.price = price;
        this.brand = brand;
        this.id = id;
        this.image = image;
        this.reviewScore = reviewScore;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getReviewScore() {
        return reviewScore;
    }

    public void setReviewScore(Double reviewScore) {
        this.reviewScore = reviewScore;
    }
}
