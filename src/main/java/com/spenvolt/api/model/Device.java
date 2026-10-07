package com.spenvolt.api.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
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
    private String power;
    private String brand;
    private String model;
    private String category;

    @ManyToOne
    @JoinColumn(name = "residence_id")
    private Residence residence;
    private List<Member> members;

    public Double calcMonthlyKwh(){
        return 0.0;
    }
    public Double calcMonthlyCost(){
        return 0.0;
    }
    
}
