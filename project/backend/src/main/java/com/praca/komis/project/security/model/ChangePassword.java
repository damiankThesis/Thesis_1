package com.praca.komis.project.security.model;

import lombok.Getter;

@Getter
public class ChangePassword {
    private String password;
    private String repeatPassword;
    private String hash;
}

