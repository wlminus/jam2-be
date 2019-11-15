package com.wlminus.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A OrderDesc.
 */
@Entity
@Table(name = "order_desc")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrderDesc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 1L)
    @Column(name = "count", nullable = false)
    private Long count;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "order_price", nullable = false)
    private Double orderPrice;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "final_price", nullable = false)
    private Double finalPrice;

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(unique = true)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("orderDescs")
    private ShopOrder shopOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCount() {
        return count;
    }

    public OrderDesc count(Long count) {
        this.count = count;
        return this;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public OrderDesc orderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
        return this;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public OrderDesc finalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
        return this;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Product getProduct() {
        return product;
    }

    public OrderDesc product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ShopOrder getShopOrder() {
        return shopOrder;
    }

    public OrderDesc shopOrder(ShopOrder shopOrder) {
        this.shopOrder = shopOrder;
        return this;
    }

    public void setShopOrder(ShopOrder shopOrder) {
        this.shopOrder = shopOrder;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDesc)) {
            return false;
        }
        return id != null && id.equals(((OrderDesc) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OrderDesc{" +
            "id=" + getId() +
            ", count=" + getCount() +
            ", orderPrice=" + getOrderPrice() +
            ", finalPrice=" + getFinalPrice() +
            "}";
    }
}
