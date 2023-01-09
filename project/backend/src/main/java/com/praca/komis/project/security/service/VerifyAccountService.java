package com.praca.komis.project.security.service;

import com.praca.komis.project.model.User;
import com.praca.komis.project.repository.UserRepository;
import com.praca.komis.project.security.model.VerifyAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerifyAccountService {

    @Autowired
    private final UserRepository userRepository;

    @Transactional
    public void confirm(VerifyAccount verifyAccount) {

        User user = userRepository
                .findByHash(verifyAccount.getHash())
                .orElseThrow(() -> new RuntimeException("Invalid link!"));

        if(user.getHashDate().plusMinutes(60).isAfter(LocalDateTime.now())){
            user.setEnabled(true);  //account activation and cleaning hash
            user.setHash(null);
            user.setHashDate(null);
        } else throw new RuntimeException("The link has expired");

    }

}
