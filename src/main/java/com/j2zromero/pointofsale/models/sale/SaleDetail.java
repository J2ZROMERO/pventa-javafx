package com.j2zromero.pointofsale.models.sale;

import java.time.LocalDateTime;
import java.util.Date;

public class SaleDetail {

    private Long id;
    private Long sellId;
    private String productCode;
    private Double amountEntered;
    private Double unitPrice;
    private Double discountLine;
    private Double taxesLine;
    private Double totalLine;
    private Date createdAt;
    private Date updatedAt;
    private String unitMeasurement;
    private Double quantity;
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

    public Date getCreatedAt() {
        return createdAt;
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

    public Double getAmountEntered() {
        return amountEntered;
    }

    public void setAmountEntered(Double amountEntered) {
        this.amountEntered = amountEntered;
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

    public Date getUpdatedAt() {
        return updatedAt;
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

    @Override
    public String toString() {
        return "SaleDetail{" +
                "id=" + id +
                ", sellId=" + sellId +
                ", productCode='" + productCode + '\'' +
                ", amount=" + amountEntered +
                ", unitPrice=" + unitPrice +
                ", discountLine=" + discountLine +
                ", taxesLine=" + taxesLine +
                ", totalLine=" + totalLine +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", unitMeasurement='" + unitMeasurement + '\'' +
                '}';
    }
}
