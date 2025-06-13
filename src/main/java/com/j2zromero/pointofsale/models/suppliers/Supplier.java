package com.j2zromero.pointofsale.models.suppliers;

import java.sql.Date;

public class Supplier {

    private int id;                  // ID único para el proveedor, llave primaria
    private String name;              // Nombre del proveedor
    private String contact;           // Información de contacto del proveedor
    private String direction;         // Dirección del proveedor
    private String extraInformation;  // Información adicional del proveedor
    private Date created_at;
    private Date updated_at;

    public Date getCreated_at() {
        return created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    // Constructor vacío
    public Supplier() {}



    // Getters y Setters para cada campo

    public Supplier(int id, String name, String contact, String direction, String extraInformation) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.direction = direction;
        this.extraInformation = extraInformation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getExtraInformation() {
        return extraInformation;
    }

    public void setExtraInformation(String extraInformation) {
        this.extraInformation = extraInformation;
    }

    // Método toString para representar el objeto como cadena de texto
    @Override
    public String toString() {
        return  name;

    }
}
