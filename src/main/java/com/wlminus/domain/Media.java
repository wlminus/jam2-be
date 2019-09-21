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
 * A Media.
 */
@Entity
@Table(name = "media")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 40)
    @Column(name = "media_name", length = 40)
    private String mediaName;

    @Size(max = 50)
    @Column(name = "media_type", length = 50)
    private String mediaType;

    @Size(max = 200)
    @Column(name = "media_url", length = 200)
    private String mediaURL;

    @Size(max = 500)
    @Column(name = "media_alt", length = 500)
    private String mediaAlt;

    @Size(max = 20)
    @Column(name = "upload_year", length = 20)
    private String uploadYear;

    @Size(max = 20)
    @Column(name = "upload_month", length = 20)
    private String uploadMonth;

    @ManyToMany(mappedBy = "media")
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

    public String getMediaName() {
        return mediaName;
    }

    public Media mediaName(String mediaName) {
        this.mediaName = mediaName;
        return this;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Media mediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public Media mediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
        return this;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public String getMediaAlt() {
        return mediaAlt;
    }

    public Media mediaAlt(String mediaAlt) {
        this.mediaAlt = mediaAlt;
        return this;
    }

    public void setMediaAlt(String mediaAlt) {
        this.mediaAlt = mediaAlt;
    }

    public String getUploadYear() {
        return uploadYear;
    }

    public Media uploadYear(String uploadYear) {
        this.uploadYear = uploadYear;
        return this;
    }

    public void setUploadYear(String uploadYear) {
        this.uploadYear = uploadYear;
    }

    public String getUploadMonth() {
        return uploadMonth;
    }

    public Media uploadMonth(String uploadMonth) {
        this.uploadMonth = uploadMonth;
        return this;
    }

    public void setUploadMonth(String uploadMonth) {
        this.uploadMonth = uploadMonth;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Media products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Media addProduct(Product product) {
        this.products.add(product);
        product.getMedia().add(this);
        return this;
    }

    public Media removeProduct(Product product) {
        this.products.remove(product);
        product.getMedia().remove(this);
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
        if (!(o instanceof Media)) {
            return false;
        }
        return id != null && id.equals(((Media) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Media{" +
            "id=" + getId() +
            ", mediaName='" + getMediaName() + "'" +
            ", mediaType='" + getMediaType() + "'" +
            ", mediaURL='" + getMediaURL() + "'" +
            ", mediaAlt='" + getMediaAlt() + "'" +
            ", uploadYear='" + getUploadYear() + "'" +
            ", uploadMonth='" + getUploadMonth() + "'" +
            "}";
    }
}
