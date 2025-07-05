package com.j2zromero.pointofsale.models.caja;

import java.util.Date;

public class SummaryCaja {
    private Long cajaId;
    private String cashier;
    private Double salesPerCaja;
    private Double subtotalCaja;
    private Double totalCajaDiscount;
    private Double totalCaja;
    private Date openedAt;
    private Date closedAt;

    public Long getCajaId() {
        return cajaId;
    }

    @Override
    public String toString() {
        return "SummaryCaja{" +
                "cajaId=" + cajaId +
                ", cashier='" + cashier + '\'' +
                ", salesPerCaja=" + salesPerCaja +
                ", subtotalCaja=" + subtotalCaja +
                ", totalCajaDiscount=" + totalCajaDiscount +
                ", totalCaja=" + totalCaja +
                ", openedAt=" + openedAt +
                ", closedAt=" + closedAt +
                '}';
    }

    public void setCajaId(Long cajaId) {
        this.cajaId = cajaId;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public Double getSalesPerCaja() {
        return salesPerCaja;
    }

    public void setSalesPerCaja(Double salesPerCaja) {
        this.salesPerCaja = salesPerCaja;
    }

    public Double getSubtotalCaja() {
        return subtotalCaja;
    }

    public void setSubtotalCaja(Double subtotalCaja) {
        this.subtotalCaja = subtotalCaja;
    }

    public Double getTotalCajaDiscount() {
        return totalCajaDiscount;
    }

    public void setTotalCajaDiscount(Double totalCajaDiscount) {
        this.totalCajaDiscount = totalCajaDiscount;
    }

    public Double getTotalCaja() {
        return totalCaja;
    }

    public void setTotalCaja(Double totalCaja) {
        this.totalCaja = totalCaja;
    }

    public Date getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(Date openedAt) {
        this.openedAt = openedAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }
}
