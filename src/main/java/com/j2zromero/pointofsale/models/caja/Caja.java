package com.j2zromero.pointofsale.models.caja;

import java.util.Date;

public class Caja {
    private Long id;
    private Long terminalId;
    private Long cashierId;
    private Double openingAmount;
    private Double closingAmount;
    private Double totalSales;
    private Double totalDiscounts;
    private Double totalTaxes;
    private Double totalCash;
    private Double totalCard;
    private Double totalTransfer;
    private Boolean isClosed;
    private String notes;

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getClosed() {
        return isClosed;
    }

    public void setClosed(Boolean closed) {
        isClosed = closed;
    }

    public Double getTotalTransfer() {
        return totalTransfer;
    }

    public void setTotalTransfer(Double totalTransfer) {
        this.totalTransfer = totalTransfer;
    }

    public Double getTotalCard() {
        return totalCard;
    }

    public void setTotalCard(Double totalCard) {
        this.totalCard = totalCard;
    }

    public Double getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(Double totalCash) {
        this.totalCash = totalCash;
    }

    public Double getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(Double totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public Double getTotalDiscounts() {
        return totalDiscounts;
    }

    public void setTotalDiscounts(Double totalDiscounts) {
        this.totalDiscounts = totalDiscounts;
    }

    public Double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Double totalSales) {
        this.totalSales = totalSales;
    }

    public Double getClosingAmount() {
        return closingAmount;
    }

    public void setClosingAmount(Double closingAmount) {
        this.closingAmount = closingAmount;
    }

    public Double getOpeningAmount() {
        return openingAmount;
    }

    public void setOpeningAmount(Double openingAmount) {
        this.openingAmount = openingAmount;
    }

    public Long getCashierId() {
        return cashierId;
    }

    public void setCashierId(Long cashierId) {
        this.cashierId = cashierId;
    }

    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Date createdAt;
    private Date updatedAt;

    public Caja() {}

    @Override
    public String toString() {
        return "CajaService[id=" + id + ", terminal=" + terminalId + ", cashier=" + cashierId + "]";
    }

}
