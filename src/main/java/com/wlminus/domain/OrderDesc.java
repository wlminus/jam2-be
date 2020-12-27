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
    @Column(name = "amount", nullable = false)
    private Long amount;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "order_price", nullable = false)
    private Double orderPrice;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "final_price", nullable = false)
    private Double finalPrice;

    @Size(max = 200)
    @Column(name = "size", length = 200)
    private String size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("shopOrders")
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ShopOrder getShopOrder() {
        return shopOrder;
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
            "id=" + id +
            ", amount=" + amount +
            ", orderPrice=" + orderPrice +
            ", finalPrice=" + finalPrice +
            ", size='" + size + '\'' +
            ", product=" + product +
            ", shopOrder=" + shopOrder +
            '}';
    }
}
