package com.spenvolt.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Residence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String address;
    private double tariffkwh;
    private double monthlyKwhLimit;
    private double monthlyCostLimit;
    private int alertThresholdPercent;
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public Double getTotalKwh(Date ref) {
        return 100.0;
    };
    public Double getTotalCost(Date ref){
        return 1000.0;
    }
    public Boolean isOverLimit(Double kwh, Double cos){
        return false;
    }
    public List<Member> getActiveMember(){
        return null;
    }
}
