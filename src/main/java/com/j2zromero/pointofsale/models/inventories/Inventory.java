package com.j2zromero.pointofsale.models.inventories;

import java.sql.Date;
import java.time.LocalDate;


public class Inventory {

    private Long id;
    private String fkProductCode;  // Foreign key to Product - mandatory
    private double stock; // mandatory
    private Date expirationDate; // Nullable
    private String location;
    private String productName;
    private String productCode;
    private String batchNumber;
    private Date createdAt;
    private Date updatedAt;
    private Boolean status;
    private Double removeStock;


    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", fkProductCode='" + fkProductCode + '\'' +
                ", stock=" + stock +
                ", expirationDate=" + expirationDate +
                ", location='" + location + '\'' +
                ", productName='" + productName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", status=" + status +
                ", removeStock=" + removeStock +
                ", unitType='" + unitType + '\'' +
                ", totalInPackage=" + totalInPackage +
                '}';
    }


    public Double getRemoveStock() {
        return removeStock;
    }

    public void setRemoveStock(Double removeStock) {
        this.removeStock = removeStock;
    }


    private String unitType;
    private Double totalInPackage;

    public Double getTotalInPackage() {
        return totalInPackage;
    }

    public void setTotalInPackage(Double totalInPackage) {
        this.totalInPackage = totalInPackage;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Constructor
    public Inventory() {
    }


    // Getters and Setters
    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFkProductCode() {
        return fkProductCode;
    }

    public void setFkProductCode(String fkProductCode) {
        this.fkProductCode = fkProductCode;
    }


    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
