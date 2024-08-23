package com.msa.auth.core;

import com.msa.auth.auditor.BaseEntity;
import com.msa.auth.auths.dto.RegisterDto;
import com.msa.auth.auths.dto.UserInfoUpdateDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    private String username;
    private String password;

    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;

    public User(RegisterDto registerDto, String encode) {
        this.username = registerDto.getUsername();
        this.password = encode;
        this.email = registerDto.getEmail();
        this.role = registerDto.getUsername().equals("choi") ? UserRoleEnum.ADMIN : UserRoleEnum.USER;
        this.firstName = registerDto.getFirstName();
        this.lastName = registerDto.getLastName();
        this.phoneNumber = registerDto.getPhoneNumber();
        this.address = registerDto.getAddress();
    }

    public User updateInfo(UserInfoUpdateDto updateDto) {
        email = updateDto.getEmail() != null ? updateDto.getEmail() : this.email;
        firstName = updateDto.getFirstName() != null ? updateDto.getFirstName() : this.firstName;
        lastName = updateDto.getLastName() != null ? updateDto.getLastName() : this.lastName;
        phoneNumber = updateDto.getPhoneNumber() != null ? updateDto.getPhoneNumber() : this.phoneNumber;
        address = updateDto.getAddress() != null ? updateDto.getAddress() : this.address;
        return this;
    }
}
