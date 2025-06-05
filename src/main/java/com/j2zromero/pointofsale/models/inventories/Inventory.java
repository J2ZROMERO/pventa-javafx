package com.j2zromero.pointofsale.models.inventories;

import java.util.Date;

public class Inventory {

    private Long id;
    private Long fkProduct;  // Foreign key to Product - mandatory
    private Double amountEntered; // mandatory
    private Double amountAvailable; // mandatory
    private Date expirationDate; // Nullable
    private String location;
    private String product_name;
    private String productCode;
    private String batch_number;
    private Date created_at;
    private Date updated_at;
    private String status;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    // Constructor
    public Inventory() {
    }

    public Inventory(String status, Date updated_at, Date created_at, String batch_number, String productCode, String product_name, String location, Date expirationDate, Double amountAvailable, Double amountEntered, Long fkProduct, Long id) {
        this.status = status;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.batch_number = batch_number;
        this.productCode = productCode;
        this.product_name = product_name;
        this.location = location;
        this.expirationDate = expirationDate;
        this.amountAvailable = amountAvailable;
        this.amountEntered = amountEntered;
        this.fkProduct = fkProduct;
        this.id = id;
    }

    // Getters and Setters
    public String getBatch_number() {
        return batch_number;
    }

    public void setBatch_number(String batch_number) {
        this.batch_number = batch_number;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFkProduct() {
        return fkProduct;
    }

    public void setFkProduct(Long fkProduct) {
        this.fkProduct = fkProduct;
    }

    public Double getAmountEntered() {
        return amountEntered;
    }

    public void setAmountEntered(Double amountEntered) {
        this.amountEntered = amountEntered;
    }

    public Double getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(Double amountAvailable) {
        this.amountAvailable = amountAvailable;
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

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", fkProduct=" + fkProduct +
                ", amountEntered=" + amountEntered +
                ", amountAvailable=" + amountAvailable +
                ", expirationDate=" + expirationDate +
                ", location='" + location + '\'' +
                '}';
    }
}
