package com.j2zromero.pointofsale.models.inventories;

import java.util.Date;

public class Inventory {

    private Long id;
    private Long fkProduct;  // Foreign key to Product
    private Date entryDate;
    private Double amountEntered;
    private Double amountAvailable;
    private Date expirationDate; // Nullable
    private String location;
    private String product_name;
    private String productCode;

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

    public Inventory(Long fkProduct, Date entryDate, Double amountEntered, Double amountAvailable, Date expirationDate, String location) {
        this.fkProduct = fkProduct;
        this.entryDate = entryDate;
        this.amountEntered = amountEntered;
        this.amountAvailable = amountAvailable;
        this.expirationDate = expirationDate;
        this.location = location;
    }

    // Getters and Setters
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

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
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
                ", entryDate=" + entryDate +
                ", amountEntered=" + amountEntered +
                ", amountAvailable=" + amountAvailable +
                ", expirationDate=" + expirationDate +
                ", location='" + location + '\'' +
                '}';
    }
}
