package com.wlminus.service.dto;

import com.wlminus.domain.District;
import com.wlminus.domain.Province;
import com.wlminus.domain.Ward;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class CartDTO {
    private Long id;

    List<ProductInCartDTO> listCard;

    @Size(max = 200)
    @NotBlank
    private String customerName;

    @Size(max = 200)
    @NotBlank
    private String mobilePhone;

    @Size(max = 1000)
    @NotBlank
    private String address;

    private Province province;
    private District district;
    private Ward ward;

    public CartDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductInCartDTO> getListCard() {
        return listCard;
    }

    public void setListCard(List<ProductInCartDTO> listCard) {
        this.listCard = listCard;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
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

    @Override
    public String toString() {
        return "CardDTO{" +
            "id=" + id +
            ", listCard=" + listCard +
            ", customerName='" + customerName + '\'' +
            ", mobilePhone='" + mobilePhone + '\'' +
            ", province='" + province + '\'' +
            ", district='" + district + '\'' +
            ", ward='" + ward + '\'' +
            '}';
    }
}
