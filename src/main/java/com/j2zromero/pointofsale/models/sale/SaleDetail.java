package com.j2zromero.pointofsale.models.sale;

import java.sql.Date;

public class SaleDetail {

    private Long id;
    private Long sellId;

    @Override
    public String toString() {
        return "SaleDetail{" +
                "id=" + id +
                ", sellId=" + sellId +
                ", productCode='" + productCode + '\'' +
                ", stock=" + stock +
                ", unitPrice=" + unitPrice +
                ", discountLine=" + discountLine +
                ", taxesLine=" + taxesLine +
                ", totalLine=" + totalLine +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", unitMeasurement='" + unitMeasurement + '\'' +
                ", quantity=" + quantity +
                ", packagePrice=" + packagePrice +
                ", lastPrice=" + lastPrice +
                ", productName='" + productName + '\'' +
                ", totalInPackage=" + totalInPackage +
                ", codePrice='" + codePrice + '\'' +
                ", hasPackageLogic=" + hasPackageLogic +
                ", originalUnitPrice=" + originalUnitPrice +
                '}';
    }

    private String productCode;
    private Double stock;
    private Double unitPrice;
    private Double discountLine;
    private Double taxesLine;
    private Double totalLine;
    private Date createdAt;
    private Date updatedAt;
    private String unitMeasurement;
    private Double quantity;
    private Double packagePrice;  //
    private Double lastPrice;
    private String productName;

    public String getCodePrice() {
        return codePrice;
    }

    public void setCodePrice(String codePrice) {
        this.codePrice = codePrice;
    }

    private Double totalInPackage;
    private String codePrice;
    public Double getTotalInPackage() {
        return totalInPackage;
    }

    public void setTotalInPackage(Double totalInPackage) {
        this.totalInPackage = totalInPackage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isHasPackageLogic() {
        return hasPackageLogic;
    }

    public void setHasPackageLogic(boolean hasPackageLogic) {
        this.hasPackageLogic = hasPackageLogic;
    }

    private boolean hasPackageLogic;
    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Double getPackagePrice() {
        return packagePrice;
    }
    private Double originalUnitPrice;

    public Double getOriginalUnitPrice() {
        return originalUnitPrice;
    }

    public void setOriginalUnitPrice(Double originalUnitPrice) {
        this.originalUnitPrice = originalUnitPrice;
    }
    public void setPackagePrice(Double packagePrice) {
        this.packagePrice = packagePrice;
    }

    public SaleDetail() {
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(String unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public String getCreatedAt() {
        return createdAt.toGMTString();
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellId() {
        return sellId;
    }

    public void setSellId(Long sellId) {
        this.sellId = sellId;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getDiscountLine() {
        return discountLine;
    }

    public void setDiscountLine(Double discountLine) {
        this.discountLine = discountLine;
    }

    public Double getTaxesLine() {
        return taxesLine;
    }

    public void setTaxesLine(Double taxesLine) {
        this.taxesLine = taxesLine;
    }

    public String getUpdatedAt() {
        return updatedAt.toGMTString();
    }



    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getTotalLine() {
        return totalLine;
    }

    public void setTotalLine(Double totalLine) {
        this.totalLine = totalLine;
    }

}
