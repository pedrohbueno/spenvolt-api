package com.spenvolt.api.dto;


import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ResidenceResponseDTO {
    private int id;
    private String name;
    private String address;
    private Double tariff;
    private Double monthlyKwhLimit;
    private Double monthlyCostLimit;
    private Integer alertThresholdPercent;
    private int ownerId;
    private LocalDateTime createdAt;
}
