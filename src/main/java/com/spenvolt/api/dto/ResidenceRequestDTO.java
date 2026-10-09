package com.spenvolt.api.dto;


import lombok.Data;

@Data
public class ResidenceRequestDTO {
    private String name;
    private String address;
    private Double tariff;
    private int ownerId;
    private Double monthlyKwhLimit;
    private Double monthlyCostLimit;
    private Integer alertThresholdPercent;
}
