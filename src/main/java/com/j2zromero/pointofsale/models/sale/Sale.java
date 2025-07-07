package com.j2zromero.pointofsale.models.sale;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sale {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long  terminalId;
    private Long cashierId;
    private Long clientId;
    private Double subtotal;
    private Long cajaId;
    private String cashierName;
    private String terminalName;
    private Double discount;
    public Double getOpeningAmount() {
        return openingAmount;
    }

    public void setOpeningAmount(Double openingAmount) {
        this.openingAmount = openingAmount;
    }

    private Double total;
    private String paymentMethod;
    private Double taxes;
    private Double openingAmount;

    public Long getCajaId() {
        return cajaId;
    }

    public void setCajaId(Long cajaId) {
        this.cajaId = cajaId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Sale() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }

    public Long getCashierId() {
        return cashierId;
    }

    public void setCashierId(Long cashierId) {
        this.cashierId = cashierId;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getTaxes() {
        return taxes;
    }

    public void setTaxes(Double taxes) {
        this.taxes = taxes;
    }

    public String getCreatedAtFormatted() {
        return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", terminalId='" + terminalId + '\'' +
                ", cashierId='" + cashierId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", subtotal=" + subtotal +
                ", discount=" + discount +
                ", total=" + total +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", taxes=" + taxes +
                '}';
    }
}
