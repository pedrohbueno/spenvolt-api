package com.spenvolt.api.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User extends Profile {
    private String passwordHash;
    private Boolean active;
    private Date lastLoginAt;

    public void activate(){
    }
    public void deactivate(){
    }
    public void changePassword(String hash){
    }


}
