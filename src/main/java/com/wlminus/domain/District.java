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
 * A District.
 */
@Entity
@Table(name = "district")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class District implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 200)
    @Column(name = "district_name", length = 200)
    private String districtName;

    @Size(max = 100)
    @Column(name = "district_type", length = 100)
    private String districtType;

    @OneToMany(mappedBy = "district")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ward> wards = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("districts")
    private Province province;

    @OneToMany(mappedBy = "district")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ShopOrder> shopOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDistrictName() {
        return districtName;
    }

    public District districtName(String districtName) {
        this.districtName = districtName;
        return this;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictType() {
        return districtType;
    }

    public District districtType(String districtType) {
        this.districtType = districtType;
        return this;
    }

    public void setDistrictType(String districtType) {
        this.districtType = districtType;
    }

    public Set<Ward> getWards() {
        return wards;
    }

    public District wards(Set<Ward> wards) {
        this.wards = wards;
        return this;
    }

    public District addWard(Ward ward) {
        this.wards.add(ward);
        ward.setDistrict(this);
        return this;
    }

    public District removeWard(Ward ward) {
        this.wards.remove(ward);
        ward.setDistrict(null);
        return this;
    }

    public void setWards(Set<Ward> wards) {
        this.wards = wards;
    }

    public Province getProvince() {
        return province;
    }

    public District province(Province province) {
        this.province = province;
        return this;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

//    public Set<Customer> getCustomers() {
//        return customers;
//    }
//
//    public District customers(Set<Customer> customers) {
//        this.customers = customers;
//        return this;
//    }
//
//    public District addCustomer(Customer customer) {
//        this.customers.add(customer);
//        customer.setDistrict(this);
//        return this;
//    }
//
//    public District removeCustomer(Customer customer) {
//        this.customers.remove(customer);
//        customer.setDistrict(null);
//        return this;
//    }
//
//    public void setCustomers(Set<Customer> customers) {
//        this.customers = customers;
//    }

    public Set<ShopOrder> getShopOrders() {
        return shopOrders;
    }

    public District shopOrders(Set<ShopOrder> shopOrders) {
        this.shopOrders = shopOrders;
        return this;
    }

    public District addShopOrder(ShopOrder shopOrder) {
        this.shopOrders.add(shopOrder);
        shopOrder.setDistrict(this);
        return this;
    }

    public District removeShopOrder(ShopOrder shopOrder) {
        this.shopOrders.remove(shopOrder);
        shopOrder.setDistrict(null);
        return this;
    }

    public void setShopOrders(Set<ShopOrder> shopOrders) {
        this.shopOrders = shopOrders;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof District)) {
            return false;
        }
        return id != null && id.equals(((District) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "District{" +
            "id=" + getId() +
            ", districtName='" + getDistrictName() + "'" +
            ", districtType='" + getDistrictType() + "'" +
            "}";
    }
}
