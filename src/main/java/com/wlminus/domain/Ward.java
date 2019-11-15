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
 * A Ward.
 */
@Entity
@Table(name = "ward")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ward implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 200)
    @Column(name = "ward_name", length = 200)
    private String wardName;

    @Size(max = 100)
    @Column(name = "ward_type", length = 100)
    private String wardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("wards")
    private District district;

    @OneToMany(mappedBy = "ward")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(mappedBy = "ward")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ShopOrder> shopOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWardName() {
        return wardName;
    }

    public Ward wardName(String wardName) {
        this.wardName = wardName;
        return this;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getWardType() {
        return wardType;
    }

    public Ward wardType(String wardType) {
        this.wardType = wardType;
        return this;
    }

    public void setWardType(String wardType) {
        this.wardType = wardType;
    }

    public District getDistrict() {
        return district;
    }

    public Ward district(District district) {
        this.district = district;
        return this;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public Ward customers(Set<Customer> customers) {
        this.customers = customers;
        return this;
    }

    public Ward addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.setWard(this);
        return this;
    }

    public Ward removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.setWard(null);
        return this;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public Set<ShopOrder> getShopOrders() {
        return shopOrders;
    }

    public Ward shopOrders(Set<ShopOrder> shopOrders) {
        this.shopOrders = shopOrders;
        return this;
    }

    public Ward addShopOrder(ShopOrder shopOrder) {
        this.shopOrders.add(shopOrder);
        shopOrder.setWard(this);
        return this;
    }

    public Ward removeShopOrder(ShopOrder shopOrder) {
        this.shopOrders.remove(shopOrder);
        shopOrder.setWard(null);
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
        if (!(o instanceof Ward)) {
            return false;
        }
        return id != null && id.equals(((Ward) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Ward{" +
            "id=" + getId() +
            ", wardName='" + getWardName() + "'" +
            ", wardType='" + getWardType() + "'" +
            "}";
    }
}
