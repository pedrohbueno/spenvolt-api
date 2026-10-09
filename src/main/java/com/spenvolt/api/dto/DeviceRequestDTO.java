package com.spenvolt.api.dto;

import java.util.Set;
import lombok.Data;

@Data
public class DeviceRequestDTO {
    private String name;
    private String brand;
    private String model;
    private String category;
    private Double power;
    private Double hoursPerDay;
    private Integer daysPerMonth;
    private String imageUrl;
    private int ownerId;
    private int residenceId;
    private Set<Integer> memberIds;
}
