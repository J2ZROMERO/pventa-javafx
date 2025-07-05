package com.j2zromero.pointofsale.models.caja;

public class SummaryDetailsCaja {
    private long cajaId;
    private String cajeroName;
    private String productCode;
    private String productName;
    private Double soldUnits;
    private Double unitPrice;
    private Double importeTotal;

    public long getCajaId() {
        return cajaId;
    }

    public void setCajaId(long cajaId) {
        this.cajaId = cajaId;
    }

    @Override
    public String toString() {
        return "SummaryDetailsCaja{" +
                "cajaId=" + cajaId +
                ", cajeroName='" + cajeroName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", soldUnits=" + soldUnits +
                ", unitPrice=" + unitPrice +
                ", importeTotal=" + importeTotal +
                '}';
    }

    public String getCajeroName() {
        return cajeroName;
    }

    public void setCajeroName(String cajeroName) {
        this.cajeroName = cajeroName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getSoldUnits() {
        return soldUnits;
    }

    public void setSoldUnits(Double soldUnits) {
        this.soldUnits = soldUnits;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
    }
}
