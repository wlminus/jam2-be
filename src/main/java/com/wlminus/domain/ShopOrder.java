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

    @Size(max = 30)
    @Column(name = "order_code", length = 30)
    private String orderCode;

    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "order_status")
    private Integer orderStatus;

    @Size(max = 300)
    @Column(name = "customer_name", length = 300)
    private String customerName;

    @Size(max = 100)
    @Column(name = "customer_phone", length = 100)
    private String customerPhone;

    @Size(max = 500)
    @Column(name = "customer_address", length = 500)
    private String customerAddress;

    @Size(max = 1000)
    @Column(name = "customer_note", length = 1000)
    private String customerNote;

    @Size(max = 100)
    @Column(name = "ship_type", length = 100)
    private String shipType;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @NotNull
    @Min(value = 1)
    @Column(name = "total_item", nullable = false)
    private Integer totalItem;

    @Column(name = "created_date")
    private Long createdDate;

    @Size(max = 200)
    @Column(name = "process_by", length = 200)
    private String processBy;

    @Column(name = "process_date")
    private Long processDate;

    @OneToMany(mappedBy = "shopOrder")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrderDesc> orderDescs = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("shopOrders")
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"shopOrders", "province"})
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"shopOrders", "district"})
    private Ward ward;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(String customerNote) {
        this.customerNote = customerNote;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(Integer totalItem) {
        this.totalItem = totalItem;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getProcessBy() {
        return processBy;
    }

    public void setProcessBy(String processBy) {
        this.processBy = processBy;
    }

    public Long getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Long processDate) {
        this.processDate = processDate;
    }

    public Set<OrderDesc> getOrderDescs() {
        return orderDescs;
    }

    public void setOrderDescs(Set<OrderDesc> orderDescs) {
        this.orderDescs = orderDescs;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
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
            "id=" + id +
            ", orderCode='" + orderCode + '\'' +
            ", orderStatus=" + orderStatus +
            ", customerName='" + customerName + '\'' +
            ", customerPhone='" + customerPhone + '\'' +
            ", customerAddress='" + customerAddress + '\'' +
            ", customerNote='" + customerNote + '\'' +
            ", shipType='" + shipType + '\'' +
            ", totalPrice=" + totalPrice +
            ", totalItem=" + totalItem +
            ", createdDate=" + createdDate +
            ", processBy='" + processBy + '\'' +
            ", processDate=" + processDate +
            ", orderDescs=" + orderDescs +
            ", province=" + province +
            ", district=" + district +
            ", ward=" + ward +
            '}';
    }
}
