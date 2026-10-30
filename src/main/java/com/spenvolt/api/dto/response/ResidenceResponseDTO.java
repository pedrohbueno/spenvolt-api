package com.spenvolt.api.dto.response;


import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ResidenceResponseDTO {
    private int id;
    private String name;
    private String address;
    private Double tariff;
    private double monthlyKwh;
    private double monthlyCost;
    private Double monthlyKwhLimit;
    private Double monthlyCostLimit;
    private Integer alertThresholdPercent;
    private int userId;
    private LocalDateTime createdAt;

}
