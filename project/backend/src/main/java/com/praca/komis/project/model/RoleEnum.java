package com.praca.komis.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private String role;
}
