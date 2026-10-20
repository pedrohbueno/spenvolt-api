package com.spenvolt.api.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryRequestDTO {
    private String type;
    private int entityId;
    private String entityName;
    private int userId;
    private int residenceId;
    private LocalDateTime createdAt;
}
