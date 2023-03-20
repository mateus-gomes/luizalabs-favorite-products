package com.mateusgomes.luizalabs.data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

@Entity
@Table(name = "products")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {

    @Id
    private UUID idProduct;

    @NotNull(message = "Product title is a required field and should not be empty")
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull(message = "Product image is a required field and should not be empty")
    @Column(name = "image", nullable = false)
    private String image;

    @NotNull(message = "Product price is a required field and should not be empty")
    @PositiveOrZero(message = "Product price should be a positive number or zero.")
    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "reviewScore", nullable = true)
    private Double reviewScore;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "fk_client",  nullable = false)
    private Client client;

    public Product() {
    }

    public Product(UUID idProduct, String title, String image, Double price, Double reviewScore, Client client) {
        this.idProduct = idProduct;
        this.title = title;
        this.image = image;
        this.price = price;
        this.reviewScore = reviewScore;
        this.client = client;
    }

    public UUID getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(UUID idProduct) {
        this.idProduct = idProduct;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getReviewScore() {
        return reviewScore;
    }

    public void setReviewScore(Double reviewScore) {
        this.reviewScore = reviewScore;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
