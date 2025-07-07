package com.j2zromero.pointofsale.models.caja;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SummaryCaja {
    private Long cajaId;
    private String userName;
    private Double openingAmount;
    private Double closingAmount;
    private Double salesCount;
    private Double subTotal;
    private Double totalDiscounts;
    private Double totalSold;
    private Double totalWithdrawals;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;

    public Long getCajaId() {
        return cajaId;
    }
    public void setCajaId(Long cajaId) {
        this.cajaId = cajaId;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getOpeningAmount() {
        return openingAmount;
    }
    public void setOpeningAmount(Double openingAmount) {
        this.openingAmount = openingAmount;
    }

    public Double getClosingAmount() {
        return closingAmount;
    }
    public void setClosingAmount(Double closingAmount) {
        this.closingAmount = closingAmount;
    }

    public Double getSalesCount() {
        return salesCount;
    }
    public void setSalesCount(Double salesCount) {
        this.salesCount = salesCount;
    }

    public Double getSubTotal() {
        return subTotal;
    }
    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getTotalDiscounts() {
        return totalDiscounts;
    }
    public void setTotalDiscounts(Double totalDiscounts) {
        this.totalDiscounts = totalDiscounts;
    }

    public Double getTotalSold() {
        return totalSold;
    }
    public void setTotalSold(Double totalSold) {
        this.totalSold = totalSold;
    }

    public Double getTotalWithdrawals() {
        return totalWithdrawals;
    }
    public void setTotalWithdrawals(Double totalWithdrawals) {
        this.totalWithdrawals = totalWithdrawals;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }
    public void setOpenedAt(LocalDateTime openedAt) {
        this.openedAt = openedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }
    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    // Formatted getters for display
    public String getOpenedAtFormatted() {
        return openedAt == null ? "" : openedAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    public String getClosedAtFormatted() {
        return closedAt == null ? "" : closedAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    @Override
    public String toString() {
        return "SummaryCaja{" +
                "cajaId=" + cajaId +
                ", userName='" + userName + '\'' +
                ", openingAmount=" + openingAmount +
                ", closingAmount=" + closingAmount +
                ", salesCount=" + salesCount +
                ", subTotal=" + subTotal +
                ", totalDiscounts=" + totalDiscounts +
                ", totalSold=" + totalSold +
                ", totalWithdrawals=" + totalWithdrawals +
                ", openedAt=" + openedAt +
                ", closedAt=" + closedAt +
                '}';
    }
}
