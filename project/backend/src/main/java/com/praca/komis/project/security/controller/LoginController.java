package com.praca.komis.project.security.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.praca.komis.project.exception.RegisterException;
import com.praca.komis.project.model.RoleEnum;
import com.praca.komis.project.model.User;
import com.praca.komis.project.repository.UserRepository;
import com.praca.komis.project.security.model.CarUserDetails;
import com.praca.komis.project.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final EmailService emailService;

    @Value("${jwt.expirationTime}")
    private long expirationTime;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${app.serviceAddress}")
    private String serviceAddress;

    @PostMapping("/login")
    public Token login(@RequestBody LoginCredentials loginCredentials) {
        return authenticate(loginCredentials.getUsername(), loginCredentials.getPassword());
    }


    @PostMapping("/register")
    public Token register(@RequestBody @Valid RegisterCredentials registerCredentials) {
       if(!registerCredentials.getPassword().equals(registerCredentials.getRepeatPassword()))
           throw new RegisterException("Passwords are different");

       if (userRepository.existsByUsername(registerCredentials.getUsername()))
           throw new RegisterException("User already exist!");

        var user = (User.builder()
                        .username(registerCredentials.getUsername())
                        .password("{bcrypt}" + new BCryptPasswordEncoder().encode(registerCredentials.getPassword()))   // encryption password
                        .name(registerCredentials.getName())
                        .surname(registerCredentials.getSurname())
                        .phone(registerCredentials.getPhone())
                        .numberOfRent(0)
                        .numberOfBuy(0)
                        .enabled(false)     //before activate account
                        .authorities(List.of(RoleEnum.ROLE_USER))
                .build());

        String hash = generateHashForConfirmAccount(user);
        user.setHash(hash);
        user.setHashDate(LocalDateTime.now());

        userRepository.save(user);
        emailService.send(registerCredentials.getUsername(), "Confirm account", createMessage(createConfirmLink(hash)));

        return authenticate(registerCredentials.getUsername(), registerCredentials.getPassword());
    }

    private String generateHashForConfirmAccount(User user) {
        String toHash = user.getId() + user.getUsername() + LocalDateTime.now();
        return DigestUtils.sha256Hex(toHash);
    }

    private String createMessage(String confirmLink) {
        return "Click on the link below to confirm account:\n" + confirmLink;
    }

    private String createConfirmLink(String hash) {
        return serviceAddress + "/confirm/" + hash;     // create activate link
    }

    private Token authenticate(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow();

        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getId(), password));     // decode

        CarUserDetails principal = (CarUserDetails) authenticate.getPrincipal();

        return new Token(       //generate JWT
                JWT.create()
                    .withSubject(String.valueOf(principal.getId()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                    .sign(Algorithm.HMAC256(secret)),
                principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(role -> RoleEnum.ROLE_ADMIN.name().equals(role))
                    .map(s -> true)
                    .findFirst()
                    .orElse(false)
                );
    }

    @Getter
    private static class LoginCredentials {
        private String username;
        private String password;
    }

    @Getter
    private static class RegisterCredentials {
        private String name;
        private String surname;
        @Email
        private String username;
        private String phone;
        @NotBlank
        private String password;
        @NotBlank
        private String repeatPassword;
    }

    @Getter
    @AllArgsConstructor
    private static class Token {
        private String token;               // convert to JSON
        private boolean hasAdminAccess;     // provide access to admin dashboard
    }
}
