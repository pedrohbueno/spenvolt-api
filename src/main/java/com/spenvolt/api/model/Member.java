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
public class Member extends Profile {
    @ManyToOne
    @JoinColumn(name = "residence_id")
    private Residence residence;

    public Double getMonthlyKwh(Date ref) {
        return 100.0;
    };
    public Double getMonthlyCost(Date ref){
        return 1000.0;
    }

    public List<Device> listDevices(){
        return null;
    }
}
