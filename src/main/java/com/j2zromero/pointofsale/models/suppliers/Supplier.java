package com.j2zromero.pointofsale.models.suppliers;

import java.sql.Date;

public class Supplier {

    private int id;                     // id
    private String name;                // name
    private String code;                // code
    private String contact;             // contact
    private String contactName;         // contact_name
    private String contactPhone;        // contact_phone
    private String direction;           // direction
    private String city;                // city
    private String extraInformation;    // extra_information
    private boolean status;             // status
    private Date created_at;            // created_at
    private Date updated_at;            // updated_at

    public Supplier() { }

    @Override
    public String toString() {
        return "Supplier{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", contact='" + contact + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", direction='" + direction + '\'' +
                ", city='" + city + '\'' +
                ", extraInformation='" + extraInformation + '\'' +
                ", status=" + status +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }

    public Supplier(int id,
                    String name,
                    String code,
                    String contact,
                    String contactName,
                    String contactPhone,
                    String direction,
                    String city,
                    String extraInformation,
                    boolean status,
                    Date created_at,
                    Date updated_at) {
        this.id               = id;
        this.name             = name;
        this.code             = code;
        this.contact          = contact;
        this.contactName      = contactName;
        this.contactPhone     = contactPhone;
        this.direction        = direction;
        this.city             = city;
        this.extraInformation = extraInformation;
        this.status           = status;
        this.created_at       = created_at;
        this.updated_at       = updated_at;
    }

    // — getters & setters —

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

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getExtraInformation() {
        return extraInformation;
    }
    public void setExtraInformation(String extraInformation) {
        this.extraInformation = extraInformation;
    }

    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }
    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

}

