package com.praca.komis.project.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class UserDTO {
    @NotBlank
    @Email
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    @NotBlank
    @Length(min = 3)
    private String name;

    @NotEmpty
    private String surname;

    @NotEmpty
    private String phone;
}
