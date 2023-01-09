package com.praca.komis.project.service;

import com.praca.komis.project.dto.UserDTO;
import com.praca.komis.project.model.RoleEnum;
import com.praca.komis.project.model.User;
import com.praca.komis.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private final UserRepository userRepository;

    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<User> editUser(Long id, UserDTO userDTO) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with this id doesn't exist"));

        user.setId(user.getId());
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setPhone(userDTO.getPhone());

        return new ResponseEntity<>(userRepository.save(user), HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<?> deleteUser(Long id) {
        // zabezpieczenie przed usunięciem konta administratora
        if (!userRepository.existsById(id)
        || userRepository.findById(id)
                .map(user -> user.getAuthorities().contains(RoleEnum.ROLE_ADMIN))
                .orElseThrow())
            return ResponseEntity.notFound().build();

        try {
            userRepository.deleteById(id);
        } catch (Exception ex) {
            logger.info(String.valueOf(ex));
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<User> getOneUser(Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> toggleUserEnabled(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            logger.info("Not Exists!");
            return ResponseEntity.notFound().build();
        }
        userRepository.findById(id)
                .ifPresent(user -> user.setEnabled(!user.getEnabled()));

        return ResponseEntity.noContent().build();
    }
}


