package com.wlminus.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 300)
    @Column(name = "name", length = 300)
    private String name;

    @Size(max = 100)
    @Column(name = "product_code", length = 100)
    private String productCode;

    @NotNull
    @Min(value = 1L)
    @Max(value = 100000000L)
    @Column(name = "price", nullable = false)
    private Long price;

    @NotNull
    @Min(value = 1L)
    @Max(value = 100000000L)
    @Column(name = "final_price", nullable = false)
    private Long finalPrice;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "discount")
    private Double discount;

    @Size(max = 200)
    @Column(name = "release_type", length = 200)
    private String releaseType;

    @Size(max = 200)
    @Column(name = "release_status", length = 200)
    private String releaseStatus;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 500)
    @Column(name = "material_desc", length = 500)
    private String materialDesc;

    @Size(max = 300)
    @Column(name = "slug", length = 300, unique = true)
    private String slug;

    @Column(name = "is_valid")
    private Boolean isValid;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("products")
    private Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "product_media",
               joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id"))
    private Set<Media> media = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "product_product_size",
               joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "product_size_id", referencedColumnName = "id"))
    private Set<ProductSize> productSizes = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "product_tag",
               joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private Set<OrderDesc> orderDesc;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCode() {
        return productCode;
    }

    public Product productCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getPrice() {
        return price;
    }

    public Product price(Long price) {
        this.price = price;
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getFinalPrice() {
        return finalPrice;
    }

    public Product finalPrice(Long finalPrice) {
        this.finalPrice = finalPrice;
        return this;
    }

    public void setFinalPrice(Long finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public Product discount(Double discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public Product releaseType(String releaseType) {
        this.releaseType = releaseType;
        return this;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public String getReleaseStatus() {
        return releaseStatus;
    }

    public Product releaseStatus(String releaseStatus) {
        this.releaseStatus = releaseStatus;
        return this;
    }

    public void setReleaseStatus(String releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public String getDescription() {
        return description;
    }

    public Product description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public Product materialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
        return this;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getSlug() {
        return slug;
    }

    public Product slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Boolean isIsValid() {
        return isValid;
    }

    public Product isValid(Boolean isValid) {
        this.isValid = isValid;
        return this;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Product createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public Product createdDate(Long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public Product modifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Long getModifiedDate() {
        return modifiedDate;
    }

    public Product modifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Category getCategory() {
        return category;
    }

    public Product category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Media> getMedia() {
        return media;
    }

    public Product media(Set<Media> media) {
        this.media = media;
        return this;
    }

    public Product addMedia(Media media) {
        this.media.add(media);
        media.getProducts().add(this);
        return this;
    }

    public Product removeMedia(Media media) {
        this.media.remove(media);
        media.getProducts().remove(this);
        return this;
    }

    public void setMedia(Set<Media> media) {
        this.media = media;
    }

    public Set<ProductSize> getProductSizes() {
        return productSizes;
    }

    public Product productSizes(Set<ProductSize> productSizes) {
        this.productSizes = productSizes;
        return this;
    }

    public Product addProductSize(ProductSize productSize) {
        this.productSizes.add(productSize);
        productSize.getProducts().add(this);
        return this;
    }

    public Product removeProductSize(ProductSize productSize) {
        this.productSizes.remove(productSize);
        productSize.getProducts().remove(this);
        return this;
    }

    public void setProductSizes(Set<ProductSize> productSizes) {
        this.productSizes = productSizes;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Product tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Product addTag(Tag tag) {
        this.tags.add(tag);
        tag.getProducts().add(this);
        return this;
    }

    public Product removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getProducts().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<OrderDesc> getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(Set<OrderDesc> orderDesc) {
        this.orderDesc = orderDesc;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", productCode='" + getProductCode() + "'" +
            ", price=" + getPrice() +
            ", finalPrice=" + getFinalPrice() +
            ", discount=" + getDiscount() +
            ", releaseType='" + getReleaseType() + "'" +
            ", releaseStatus='" + getReleaseStatus() + "'" +
            ", description='" + getDescription() + "'" +
            ", materialDesc='" + getMaterialDesc() + "'" +
            ", slug='" + getSlug() + "'" +
            ", isValid='" + isIsValid() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate=" + getCreatedDate() +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", modifiedDate=" + getModifiedDate() +
            "}";
    }
}
