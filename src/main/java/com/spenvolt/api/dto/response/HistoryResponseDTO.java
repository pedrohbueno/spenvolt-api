package com.spenvolt.api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryResponseDTO {
    private int id;
    private String type;
    private int entityId;
    private String entityName;
    private int userId;
    private int residenceId;
    private LocalDateTime createdAt;
}
