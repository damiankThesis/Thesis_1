package com.praca.komis.project.controller;

import com.praca.komis.project.dto.RentDTO;
import com.praca.komis.project.model.Rent;
import com.praca.komis.project.service.RentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/rent")
@RequiredArgsConstructor
public class RentController {

    private static final Logger logger = LoggerFactory.getLogger(RentController.class);

    @Autowired
    private final RentService rentService;

    @GetMapping
    @Cacheable("rents")
    public ResponseEntity<List<Rent>> getAllRent() {
        logger.info("GET /api/v1/rent");
        return rentService.getAllRent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rent> getOneRent(@PathVariable Long id) {
        logger.info("GET /api/v1/rent/" + id);
        return rentService.getOneRent(id);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Rent>> getRentsByUser(@PathVariable Long id) {
        logger.info("GET /api/v1/rent/user/" + id);
        return rentService.getRentByUser(id);
    }

    @GetMapping("/car/{id}")
    public ResponseEntity<List<Rent>> getRentsByCar(@PathVariable Long id) {
        logger.info("GET /api/v1/rent/car/" + id);
        return rentService.getRentByCar(id);
    }

    @GetMapping("/{id}/date")
    public ResponseEntity<List<String[]>> getOneRentDate(@PathVariable Long id) {
        logger.info("GET /api/v1/rent/" + id + "/date");
        return rentService.getOneRentDate(id);
    }

//    @CacheEvict(cacheNames = "rents")
    @Transactional
    @PostMapping
    public ResponseEntity<?> addRent(@RequestBody RentDTO rentDTO, @AuthenticationPrincipal Long userId) {
        logger.info("POST /api/v1/rent");
        return rentService.saveRent(rentDTO, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRent(@PathVariable Long id) {
        logger.info("DELETE /api/v1/rent/" + id);
        return rentService.deleteRent(id);
    }

}
