package com.j2zromero.pointofsale.models.user;

import java.sql.Date;

public class User {

        private Long id;
        private String name;
        private String fkRoleCode;
        private String email;
        private String phone;
        private Boolean status;
        private Date createdAt;
        private Date updatedAt;
        private String password;



        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getFkRoleCode() { return fkRoleCode; }
        public void setFkRoleCode(String fkRoleCode) { this.fkRoleCode = fkRoleCode; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public Boolean getStatus() { return status; }
        public void setStatus(Boolean status) { this.status = status; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fkRoleCode='" + fkRoleCode + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", password='" + password + '\'' +
                '}';
    }

    public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
