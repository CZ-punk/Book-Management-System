package com.msa.auth.auths.dto;

import lombok.Data;

@Data
public class RegisterDto {

    private String username;
    private String password;

    private String email;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
}
