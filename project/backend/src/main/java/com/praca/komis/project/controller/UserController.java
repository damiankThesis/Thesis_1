package com.praca.komis.project.controller;

import com.praca.komis.project.dto.UserDTO;
import com.praca.komis.project.model.User;
import com.praca.komis.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        logger.info("GET /api/v1/users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getOneUser(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        logger.info("GET /api/v1/users/" + userId);
        return userService.getOneUser(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> editUser(@PathVariable Long id, @RequestBody UserDTO userDTO, @AuthenticationPrincipal Long userId) {
        logger.info("PUT /api/v1/users/" + userId);
        return userService.editUser(userId, userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("DELETE /api/v1/users/" + id);
        return userService.deleteUser(id);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> blockUser(@PathVariable Long id) {
        logger.info("PATCH /api/v1/users/" + id);
        return userService.toggleUserEnabled(id);
    }

}
