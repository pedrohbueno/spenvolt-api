package com.spenvolt.api.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String source;
    private Float price;
    private Float originalPrice;
    private String imageUrl;
    private String deviceUrl;
    private Double power;
    private String brand;
    private String model;
    private String category;
    private Double tariff;

    @ManyToOne
    @JoinColumn(name = "residence_id")
    private Residence residence;
    @ManyToMany
    @JoinTable(
            name = "device_members",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<Member> members;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Double calcMonthlyKwh(){
        return 0.0;
    }
    public Double calcMonthlyCost(Double tariff){
        return 0.0;
    }
    
}
