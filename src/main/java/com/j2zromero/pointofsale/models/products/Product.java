package com.j2zromero.pointofsale.models.products;

import java.sql.Date;

public class Product {

    private long id;
    private String name;
    private String description;
    private String code;
    private String unitMeasurement;
    private Double unitPrice;
    private Double volumePrice; // Nullable
    private Double stock; // Nullable
    private String category;
    private String brand;
    private Long fkSupplier; // ID del proveedor
    private Date updatedAt;
    private Date createdAt;
    private boolean hasPackageLogic;
    private Double totalInPackage;

    public boolean isHasPackageLogic() {
        return hasPackageLogic;
    }

    public void setHasPackageLogic(boolean hasPackageLogic) {
        this.hasPackageLogic = hasPackageLogic;
    }

    public Double getTotalInPackage() {
        return totalInPackage;
    }

    public void setTotalInPackage(Double totalInPackage) {
        this.totalInPackage = totalInPackage;
    }

    public Double getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(Double packagePrice) {
        this.packagePrice = packagePrice;
    }

    private String supplierName;
    private Double packagePrice;

    public Product() {
    }

    // Getters y Setters para cada atributo
    public long getId() {
        return id;
    }

    public String getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(String unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Product(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getVolumePrice() {
        return volumePrice;
    }

    public void setVolumePrice(Double volumePrice) {
        this.volumePrice = volumePrice;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", code='" + code + '\'' +
                ", unitMeasurement='" + unitMeasurement + '\'' +
                ", unitPrice=" + unitPrice +
                ", volumePrice=" + volumePrice +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", fkSupplier=" + fkSupplier +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", hasPackageLogic=" + hasPackageLogic +
                ", totalInPackage=" + totalInPackage +
                ", supplierName='" + supplierName + '\'' +
                ", packagePrice=" + packagePrice +
                '}';
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getFkSupplier() {
        return fkSupplier;
    }

    public void setFkSupplier(Long fkSupplier) {
        this.fkSupplier = fkSupplier;
    }

}