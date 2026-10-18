package com.spenvolt.api.dto.request;


import lombok.Data;

@Data
public class ResidenceRequestDTO {
    private String name;
    private String address;
    private Double tariff;
    private Double monthlyKwhLimit;
    private Double monthlyCostLimit;
    private Integer alertThresholdPercent;
}
