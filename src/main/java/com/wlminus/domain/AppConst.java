package com.wlminus.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.wlminus.domain.enumeration.ConfigKey;

/**
 * A AppConst.
 */
@Entity
@Table(name = "app_const")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AppConst implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "const_key")
    private ConfigKey constKey;

    @Size(max = 1000)
    @Column(name = "const_value", length = 1000)
    private String constValue;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConfigKey getConstKey() {
        return constKey;
    }

    public AppConst constKey(ConfigKey constKey) {
        this.constKey = constKey;
        return this;
    }

    public void setConstKey(ConfigKey constKey) {
        this.constKey = constKey;
    }

    public String getConstValue() {
        return constValue;
    }

    public AppConst constValue(String constValue) {
        this.constValue = constValue;
        return this;
    }

    public void setConstValue(String constValue) {
        this.constValue = constValue;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppConst)) {
            return false;
        }
        return id != null && id.equals(((AppConst) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AppConst{" +
            "id=" + getId() +
            ", constKey='" + getConstKey() + "'" +
            ", constValue='" + getConstValue() + "'" +
            "}";
    }
}
