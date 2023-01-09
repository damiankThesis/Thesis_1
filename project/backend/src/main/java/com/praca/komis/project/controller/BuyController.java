package com.praca.komis.project.controller;

import com.praca.komis.project.dto.BuyDTO;
import com.praca.komis.project.model.Buy;
import com.praca.komis.project.service.BuyService;
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
@RequestMapping("/api/v1/buy")
@RequiredArgsConstructor
public class BuyController {

    private static final Logger logger = LoggerFactory.getLogger(BuyController.class);

    @Autowired
    private BuyService buyService;

    @GetMapping
    @Cacheable("buys")
    public ResponseEntity<List<Buy>> getAllBuy() {
        logger.info("GET /api/v1/buy");
        return buyService.getAllBuy();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Buy> getOneBuy(@PathVariable Long id) {
        logger.info("GET /api/v1/buy" + id);
        return buyService.getOneBuy(id);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Buy>> getBuyByUser(@PathVariable Long id) {
        logger.info("GET /api/v1/buy/user" + id);
        return buyService.getBuyByUser(id);
    }

    @GetMapping("/car/{id}")
    public ResponseEntity<List<Buy>> getBuyByCar(@PathVariable Long id) {
        logger.info("GET /api/v1/buy/car" + id);
        return buyService.getBuyByCar(id);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> addBuy(@RequestBody BuyDTO buyDTO, @AuthenticationPrincipal Long userId) {
        logger.info("POST /api/v1/buy");
        return buyService.saveBuy(buyDTO, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBuy(@PathVariable Long id) {
        logger.info("DELETE /api/v1/buy/" + id);
        return buyService.deleteBuy(id);
    }

}
