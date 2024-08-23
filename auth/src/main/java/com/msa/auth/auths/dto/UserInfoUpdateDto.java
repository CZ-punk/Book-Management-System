package com.msa.auth.auths.dto;


import com.msa.auth.core.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoUpdateDto {

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;

}
