package com.spenvolt.api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryResponseDTO {
    private String message;
    private int userId;
    private int residenceId;
    private LocalDateTime createdAt;
}
