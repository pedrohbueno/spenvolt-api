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
@Table(name = "members")
public class Member extends Profile {
    @ManyToOne
    @JoinColumn(name = "residence_id")
    private Residence residence;

    public Double getMonthlyKwh(LocalDateTime ref) {
        return 100.0;
    };
    public Double getMonthlyCost(LocalDateTime ref){
        return 1000.0;
    }

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public List<Device> listDevices(){
        return null;
    }
}
