package com.project.system.storemanagement.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * 商品
 */
public class GoodsBean implements MultiItemEntity, Serializable {
    int itemType = -0xff;

    /**
     * id : d0bb8fa4229240c38bfdb346d708cd55
     * name : 香蕉
     * unit : 吨
     * remark : 新鲜的香蕉
     * userId : 65a16152ff2043d8a558d7086eba9e13
     * createDate : 2019-05-23T11:45:50.000+0000
     */

    private String id;
    private String name;
    private String unit;
    private String remark;
    private String userId;
    private String createDate;
    private String linkMan;
    private String telNumber;
    private String address;
    private String postCode;

    private String supplierName;
    private String goodName;
    private String supplyNum;
    private String supplyUnivalence;
    private String demandUnivalence;
    private String customerName;

    private String demandNum;

    private String goodUnit;
    private String storeNum;
    private String goodId;
    private String supplierId;
    private String customerId;
    private String demandDate;
    private String supplyDate;

    public String getDemandDate() {
        return demandDate;
    }

    public void setDemandDate(String demandDate) {
        this.demandDate = demandDate;
    }

    public String getSupplyDate() {
        return supplyDate;
    }

    public void setSupplyDate(String supplyDate) {
        this.supplyDate = supplyDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getGoodUnit() {
        return goodUnit;
    }

    public void setGoodUnit(String goodUnit) {
        this.goodUnit = goodUnit;
    }

    public String getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(String storeNum) {
        this.storeNum = storeNum;
    }

    public String getDemandUnivalence() {
        return demandUnivalence;
    }

    public void setDemandUnivalence(String demandUnivalence) {
        this.demandUnivalence = demandUnivalence;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDemandNum() {
        return demandNum;
    }

    public void setDemandNum(String demandNum) {
        this.demandNum = demandNum;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getSupplyNum() {
        return supplyNum;
    }

    public void setSupplyNum(String supplyNum) {
        this.supplyNum = supplyNum;
    }

    public String getSupplyUnivalence() {
        return supplyUnivalence;
    }

    public void setSupplyUnivalence(String supplyUnivalence) {
        this.supplyUnivalence = supplyUnivalence;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
