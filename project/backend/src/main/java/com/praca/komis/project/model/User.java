package com.praca.komis.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @JsonIgnore
    private String password;
    private String name;
    private String surname;

    @NotNull
    @Column(unique = true)
    @Email
    private String username;
    @NotNull
    private Boolean enabled;

    @JsonIgnore
    @ElementCollection      //one to many
    @CollectionTable(name="authorities", joinColumns = @JoinColumn(name = "username", referencedColumnName = "username" ))
    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    private List<RoleEnum> authorities;

    @Pattern(regexp = "^[0-9]{9}", message = "phone number must have 9 digits!")
    @Column(length = 9)
    private String phone;
    private Integer numberOfRent;
    private Integer numberOfBuy;

    private String hash;
    private LocalDateTime HashDate;
}
