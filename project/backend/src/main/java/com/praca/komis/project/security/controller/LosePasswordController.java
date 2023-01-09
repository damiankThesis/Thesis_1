package com.praca.komis.project.security.controller;

import com.praca.komis.project.security.model.ChangePassword;
import com.praca.komis.project.security.model.EmailObj;
import com.praca.komis.project.security.service.LosePasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("/api/v1"))
@RequiredArgsConstructor
@CrossOrigin
public class LosePasswordController {

    @Autowired
    private final LosePasswordService losePasswordService;

    @PostMapping("/lostPassword")
    public void lostPassword(@RequestBody EmailObj emailObject) {
        losePasswordService.sendLostPasswordLink(emailObject);
    }

    @PostMapping("/changePassword")
    public void changePassword(@RequestBody ChangePassword changePassword) {
        losePasswordService.changePassword(changePassword);
    }

}
