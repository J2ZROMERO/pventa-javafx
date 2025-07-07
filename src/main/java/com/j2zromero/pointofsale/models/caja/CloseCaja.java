package com.j2zromero.pointofsale.models.caja;

public class CloseCaja {
 private double totalSales;
 private double totalDiscount;

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getOpeningAmount() {
        return openingAmount;
    }

    public void setOpeningAmount(double openingAmount) {
        this.openingAmount = openingAmount;
    }

    public double getTotalWithdrawl() {
        return totalWithdrawl;
    }

    public void setTotalWithdrawl(double totalWithdrawl) {
        this.totalWithdrawl = totalWithdrawl;
    }

    @Override
    public String toString() {
        return "CloseCaja{" +
                "totalSales=" + totalSales +
                ", totalDiscount=" + totalDiscount +
                ", openingAmount=" + openingAmount +
                ", totalWithdrawl=" + totalWithdrawl +
                '}';
    }

    private double openingAmount;
 private double totalWithdrawl;
}
