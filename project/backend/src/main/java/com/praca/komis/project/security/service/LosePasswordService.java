package com.praca.komis.project.security.service;

import com.praca.komis.project.model.User;
import com.praca.komis.project.repository.UserRepository;
import com.praca.komis.project.security.model.ChangePassword;
import com.praca.komis.project.security.model.EmailObj;
import com.praca.komis.project.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LosePasswordService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final EmailService emailService;

    @Value("${app.serviceAddress}")
    private String serviceAddress;

    @Transactional
    public void sendLostPasswordLink(EmailObj email) {
        User user = userRepository.findByUsername(email.getUsername())
                .orElseThrow(() -> new RuntimeException("This email does not exist!"));

        if (user.getEnabled()) {
            String hash = generateHashForLostPassword(user);
            user.setHash(hash);
            user.setHashDate(LocalDateTime.now());
            emailService.send(email.getUsername(), "Reset your password", createMessage(createLink(hash)));
        }
    }

    @Transactional
    public void changePassword(ChangePassword changePassword) {
        if (!Objects.equals(changePassword.getPassword(), changePassword.getRepeatPassword()))
            throw new RuntimeException("Passwords are not the same");

        User user = userRepository
                .findByHash(changePassword.getHash())
                .orElseThrow(() -> new RuntimeException("Invalid link"));
        if (user.getHashDate().plusMinutes(10).isAfter(LocalDateTime.now())) {
            user.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(changePassword.getPassword()));
            user.setHash(null);
            user.setHashDate(null);
        } else throw new RuntimeException("The link has expired");

    }

    private String generateHashForLostPassword(User user) {
        String toHash = user.getId() + user.getUsername() + user.getPassword() + LocalDateTime.now();
        return DigestUtils.sha256Hex(toHash);
    }

    private String createLink(String hash) {
        return serviceAddress + "/lostPassword/" + hash;
    }

    private String createMessage(String hashLink) {
        return "Link to change your password has been generated \n\nClick link to reset your password:\n" + hashLink;
    }

}
