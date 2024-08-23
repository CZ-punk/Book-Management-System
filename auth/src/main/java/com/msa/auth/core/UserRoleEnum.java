package com.msa.auth.core;

import lombok.Getter;


@Getter
public enum UserRoleEnum {
    USER(Authority.USER), ADMIN(Authority.ADMIN);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    private static class Authority {
        private static final String USER = "ROLE_USER";
        private static final String ADMIN = "ROLE_ADMIN";
    }
}
