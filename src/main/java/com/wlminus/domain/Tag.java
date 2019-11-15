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
 * A Tag.
 */
@Entity
@Table(name = "tag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 300)
    @Column(name = "tag", length = 300)
    private String tag;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @ManyToMany(mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<ShopNew> news = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public Tag tag(String tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public Tag description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Tag products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Tag addProduct(Product product) {
        this.products.add(product);
        product.getTags().add(this);
        return this;
    }

    public Tag removeProduct(Product product) {
        this.products.remove(product);
        product.getTags().remove(this);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<ShopNew> getNews() {
        return news;
    }

    public Tag news(Set<ShopNew> shopNews) {
        this.news = shopNews;
        return this;
    }

    public Tag addNew(ShopNew shopNew) {
        this.news.add(shopNew);
        shopNew.getTags().add(this);
        return this;
    }

    public Tag removeNew(ShopNew shopNew) {
        this.news.remove(shopNew);
        shopNew.getTags().remove(this);
        return this;
    }

    public void setNews(Set<ShopNew> shopNews) {
        this.news = shopNews;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return id != null && id.equals(((Tag) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", tag='" + getTag() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
