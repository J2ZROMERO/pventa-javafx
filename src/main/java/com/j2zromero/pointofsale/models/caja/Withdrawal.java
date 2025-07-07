package com.j2zromero.pointofsale.models.caja;

import java.time.LocalDateTime;
import java.util.Date;

public class Withdrawal {
    private Long id;
    private double amount;
    private Date createdAt;
    private Date updatedAt;
    private String reason;
    private Long cajaId;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Withdrawal{" +
                "id=" + id +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", reason='" + reason + '\'' +
                ", cajaId=" + cajaId +
                '}';
    }

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

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public Long getCajaId() {
        return cajaId;
    }

    public void setCajaId(Long cajaId) {
        this.cajaId = cajaId;
    }
}
