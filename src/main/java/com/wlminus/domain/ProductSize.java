package com.wlminus.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ProductSize.
 */
@Entity
@Table(name = "product_size")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductSize implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 200)
    @Column(name = "size_name", length = 200)
    private String sizeName;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @ManyToMany(mappedBy = "productSizes")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSizeName() {
        return sizeName;
    }

    public ProductSize sizeName(String sizeName) {
        this.sizeName = sizeName;
        return this;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getDescription() {
        return description;
    }

    public ProductSize description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public ProductSize products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public ProductSize addProduct(Product product) {
        this.products.add(product);
        product.getProductSizes().add(this);
        return this;
    }

    public ProductSize removeProduct(Product product) {
        this.products.remove(product);
        product.getProductSizes().remove(this);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductSize)) {
            return false;
        }
        return id != null && id.equals(((ProductSize) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProductSize{" +
            "id=" + getId() +
            ", sizeName='" + getSizeName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
