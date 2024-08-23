package com.msa.review.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;

}
