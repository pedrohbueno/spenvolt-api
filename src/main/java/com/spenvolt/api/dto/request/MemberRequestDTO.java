package com.spenvolt.api.dto.request;


import lombok.Data;

@Data
public class MemberRequestDTO {
    private String name;
    private String email;
    private String photoUrl;
    private int residenceId;
}
