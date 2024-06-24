package com.bhq.ius.integration.dto;

import com.bhq.ius.constant.IusConstant;
import lombok.Data;

@Data
public class MoodleUser {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    /* Lấy theo madk <MA_DK>06003-20201023-083419</MA_DK> */
    private String idNumber;

    public String setEmail(String email) {
        return email + IusConstant.EMAIL_PARTERN;
    }
}
