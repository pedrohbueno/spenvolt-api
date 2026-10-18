package com.spenvolt.api.dto.response;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberResponseDTO {
    private int id;
    private String name;
    private String email;
    private String photoUrl;
    private int residenceId;
    private LocalDateTime createdAt;


}
