package com.wlminus.service.dto;

import com.wlminus.domain.Product;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ProductInCartDTO {
    private Product product;

    @NotNull
    @Min(value = 1L)
    @Max(value = 100000000L)
    private Long amount;

    private Double orderPrice;
    private Double finalPrice;
    private String size;

    public ProductInCartDTO() {
    }

    public ProductInCartDTO(Product product, @NotNull @Min(value = 1L) @Max(value = 100000000L) Long amount, Double orderPrice, Double finalPrice, String size) {
        this.product = product;
        this.amount = amount;
        this.orderPrice = orderPrice;
        this.finalPrice = finalPrice;
        this.size = size;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
