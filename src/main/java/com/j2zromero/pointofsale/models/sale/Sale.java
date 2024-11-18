package com.j2zromero.pointofsale.models.sale;

import java.util.Date;

public class Sale {
    private Long id;
    private Long idProduct;
    private Double soldAmount;
    private Double unitPrice;
    private Double totalSale;
    private Date date;
    private String saleType;
    private String productName;
    private Double amountAvailable;

    public Double getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(Double amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    // Default constructor
    public Sale() {
    }

    // Constructor with parameters
    public Sale(Long idProduct, Double soldAmount, Double unitPrice, Double totalSale, String saleType,String productName,Double available) {
        this.idProduct = idProduct;
        this.soldAmount = soldAmount;
        this.unitPrice = unitPrice;
        this.totalSale = totalSale;
        this.saleType = saleType;
        this.productName = productName;
        this.amountAvailable = available;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public Double getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(Double soldAmount) {
        this.soldAmount = soldAmount;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(Double totalSale) {
        this.totalSale = totalSale;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }
}
