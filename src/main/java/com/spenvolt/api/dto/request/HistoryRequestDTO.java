package com.spenvolt.api.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryRequestDTO {
    private String type;
    private int userId;
    private int residenceId;
    private LocalDateTime createdAt;
}
