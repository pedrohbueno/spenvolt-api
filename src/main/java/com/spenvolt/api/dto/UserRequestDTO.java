package com.spenvolt.api.dto;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;
    private String photoUrl;
}
