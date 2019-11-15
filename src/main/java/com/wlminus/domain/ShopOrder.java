package com.wlminus.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ShopOrder.
 */
@Entity
@Table(name = "shop_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ShopOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @Column(name = "order_status", length = 100)
    private String orderStatus;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Size(max = 200)
    @Column(name = "created_by", length = 200)
    private String createdBy;

    @Column(name = "created_date")
    private Long createdDate;

    @Size(max = 200)
    @Column(name = "modified_by", length = 200)
    private String modifiedBy;

    @Column(name = "modified_date")
    private Long modifiedDate;

    @OneToMany(mappedBy = "shopOrder")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrderDesc> orderDescs = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("shopOrders")
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("shopOrders")
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("shopOrders")
    private Ward ward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("shopOrders")
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public ShopOrder orderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public ShopOrder totalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ShopOrder createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public ShopOrder createdDate(Long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public ShopOrder modifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Long getModifiedDate() {
        return modifiedDate;
    }

    public ShopOrder modifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Set<OrderDesc> getOrderDescs() {
        return orderDescs;
    }

    public ShopOrder orderDescs(Set<OrderDesc> orderDescs) {
        this.orderDescs = orderDescs;
        return this;
    }

    public ShopOrder addOrderDesc(OrderDesc orderDesc) {
        this.orderDescs.add(orderDesc);
        orderDesc.setShopOrder(this);
        return this;
    }

    public ShopOrder removeOrderDesc(OrderDesc orderDesc) {
        this.orderDescs.remove(orderDesc);
        orderDesc.setShopOrder(null);
        return this;
    }

    public void setOrderDescs(Set<OrderDesc> orderDescs) {
        this.orderDescs = orderDescs;
    }

    public Province getProvince() {
        return province;
    }

    public ShopOrder province(Province province) {
        this.province = province;
        return this;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District getDistrict() {
        return district;
    }

    public ShopOrder district(District district) {
        this.district = district;
        return this;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Ward getWard() {
        return ward;
    }

    public ShopOrder ward(Ward ward) {
        this.ward = ward;
        return this;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ShopOrder customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopOrder)) {
            return false;
        }
        return id != null && id.equals(((ShopOrder) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShopOrder{" +
            "id=" + getId() +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate=" + getCreatedDate() +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", modifiedDate=" + getModifiedDate() +
            "}";
    }
}
