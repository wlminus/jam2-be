package com.wlminus.service.dto;

import com.wlminus.domain.District;
import com.wlminus.domain.Province;
import com.wlminus.domain.Ward;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class CartDTO {
    List<ProductInCartDTO> orderList;

    @Size(max = 200)
    @NotBlank
    private String customerName;

    @Size(max = 200)
    @NotBlank
    private String customerPhone;

    @Size(max = 1000)
    private String customerAddress;

    @Size(max = 1000)
    private String customerNote;

    private long shipType;

    private Province province;
    private District district;
    private Ward ward;

    public CartDTO() {
    }

    public CartDTO(List<ProductInCartDTO> orderList, @Size(max = 200) @NotBlank String customerName, @Size(max = 200) @NotBlank String customerPhone, @Size(max = 1000) String customerAddress, @Size(max = 1000) String customerNote, long shipType, Province province, District district, Ward ward) {
        this.orderList = orderList;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.customerNote = customerNote;
        this.shipType = shipType;
        this.province = province;
        this.district = district;
        this.ward = ward;
    }

    public List<ProductInCartDTO> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<ProductInCartDTO> orderList) {
        this.orderList = orderList;
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

    public long getShipType() {
        return shipType;
    }

    public void setShipType(long shipType) {
        this.shipType = shipType;
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
}
