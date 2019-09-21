package com.wlminus.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Province.
 */
@Entity
@Table(name = "province")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Province implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 200)
    @Column(name = "province_name", length = 200)
    private String provinceName;

    @Size(max = 100)
    @Column(name = "province_type", length = 100)
    private String provinceType;

    @OneToMany(mappedBy = "province")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<District> districts = new HashSet<>();

    @OneToMany(mappedBy = "province")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(mappedBy = "province")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ShopOrder> shopOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public Province provinceName(String provinceName) {
        this.provinceName = provinceName;
        return this;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceType() {
        return provinceType;
    }

    public Province provinceType(String provinceType) {
        this.provinceType = provinceType;
        return this;
    }

    public void setProvinceType(String provinceType) {
        this.provinceType = provinceType;
    }

    public Set<District> getDistricts() {
        return districts;
    }

    public Province districts(Set<District> districts) {
        this.districts = districts;
        return this;
    }

    public Province addDistrict(District district) {
        this.districts.add(district);
        district.setProvince(this);
        return this;
    }

    public Province removeDistrict(District district) {
        this.districts.remove(district);
        district.setProvince(null);
        return this;
    }

    public void setDistricts(Set<District> districts) {
        this.districts = districts;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public Province customers(Set<Customer> customers) {
        this.customers = customers;
        return this;
    }

    public Province addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.setProvince(this);
        return this;
    }

    public Province removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.setProvince(null);
        return this;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public Set<ShopOrder> getShopOrders() {
        return shopOrders;
    }

    public Province shopOrders(Set<ShopOrder> shopOrders) {
        this.shopOrders = shopOrders;
        return this;
    }

    public Province addShopOrder(ShopOrder shopOrder) {
        this.shopOrders.add(shopOrder);
        shopOrder.setProvince(this);
        return this;
    }

    public Province removeShopOrder(ShopOrder shopOrder) {
        this.shopOrders.remove(shopOrder);
        shopOrder.setProvince(null);
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
        if (!(o instanceof Province)) {
            return false;
        }
        return id != null && id.equals(((Province) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Province{" +
            "id=" + getId() +
            ", provinceName='" + getProvinceName() + "'" +
            ", provinceType='" + getProvinceType() + "'" +
            "}";
    }
}
