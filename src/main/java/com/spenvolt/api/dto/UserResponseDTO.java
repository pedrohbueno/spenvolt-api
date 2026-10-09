package com.spenvolt.api.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserResponseDTO {
    private int id;
    private String name;
    private String email;
    private String photoUrl;
    private boolean active;
    private LocalDateTime createdAt;
}
