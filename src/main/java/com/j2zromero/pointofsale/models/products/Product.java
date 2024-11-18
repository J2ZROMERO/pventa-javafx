package com.j2zromero.pointofsale.models.products;

public class Product {

    private long id;
    private String name;
    private String description;
    private String code;
    private Integer unitMeasurement;
    private Double unitPrice;
    private Double volumePrice; // Nullable
    private Double stock; // Nullable
    private String category;
    private String brand;
    private Long fkSupplier; // ID del proveedor

    public Product() {
    }

    public Product(String name, String description, String code, Integer unitMeasurement, Double unitPrice, Double volumePrice, Double stock, String category, String brand, long fkSupplier) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.unitMeasurement = unitMeasurement;
        this.unitPrice = unitPrice;
        this.volumePrice = volumePrice;
        this.stock = stock;
        this.category = category;
        this.brand = brand;
        this.fkSupplier = fkSupplier;
    }

    // Getters y Setters para cada atributo
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Integer getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(Integer unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
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

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getFkSupplier() {
        return fkSupplier;
    }

    public void setFkSupplier(Long fkSupplier) {
        this.fkSupplier = fkSupplier;
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
                '}';
    }
}