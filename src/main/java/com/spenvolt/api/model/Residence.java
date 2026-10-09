package com.spenvolt.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "residences")
public class Residence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String address;
    private double tariff;
    private double monthlyKwhLimit;
    private double monthlyCostLimit;
    private int alertThresholdPercent;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Double getTotalKwh(LocalDateTime ref) {
        return 100.0;
    };
    public Double getTotalCost(LocalDateTime ref){
        return 1000.0;
    }
    public Boolean isOverLimit(Double kwh, Double cos){
        return false;
    }
    public List<Member> getActiveMember(){
        return null;
    }
}
