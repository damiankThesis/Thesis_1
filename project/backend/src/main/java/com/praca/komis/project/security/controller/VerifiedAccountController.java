package com.praca.komis.project.security.controller;

import com.praca.komis.project.security.model.VerifyAccount;
import com.praca.komis.project.security.service.VerifyAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("/api/v1"))
@RequiredArgsConstructor
@CrossOrigin
public class VerifiedAccountController {

    @Autowired
    private final VerifyAccountService verifyAccountService;

    @PostMapping("/verified")
    public void verifiedAccount(@RequestBody VerifyAccount verifyAccount) {
        verifyAccountService.confirm(verifyAccount);
    }

}
