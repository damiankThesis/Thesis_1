package com.praca.komis.project.controller;

import com.praca.komis.project.dto.CarDTO;
import com.praca.komis.project.dto.UploadImageDTO;
import com.praca.komis.project.model.Car;
import com.praca.komis.project.service.CarService;
import com.praca.komis.project.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController {

    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private final CarService carService;

    @Autowired
    private ImageService imageService;


    @GetMapping()
    public Page<?> getAllCarsWithPage(Pageable pageable) {
        logger.info("GET /api/v1/cars");
        return carService.getAllCars(pageable);
    }

    @GetMapping("/available")
    public Page<?> getAvailableCars(Pageable pageable) {
        logger.info("GET /api/v1/cars/available");
        return carService.getAvailableCar(pageable);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCars() {
        logger.info("GET /api/v1/cars/all");
        return carService.getAllCars();
    }

    @GetMapping("/details")
    public ResponseEntity<?> getCarDetails() {
        logger.info("GET /api/v1/cars/details");
        return ResponseEntity.ok(carService.getCarDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneCar(@PathVariable long id) {
        logger.info("GET /api/v1/cars/" + id);
        return carService.getCarById(id);
    }

    @PostMapping
    public ResponseEntity<Car> saveCar(@RequestBody @Valid CarDTO carDto) {
        logger.info("POST /api/v1/cars/");
        return carService.saveCar(carDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable long id, @RequestBody @Valid CarDTO carDto) {
        logger.info("PUT /api/v1/cars/" + id);
        return carService.updateCar(id, carDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        logger.info("DELETE /api/v1/cars/" + id);
        return carService.deleteCar(id);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleCarStatus(@PathVariable Long id) {
        logger.info("PATCH /api/v1/cars/" + id);
        return carService.toggleCarStatus(id);
    }

    @PostMapping("/upload")
    public UploadImageDTO uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        logger.info("Upload image " + multipartFile.getOriginalFilename());
        return imageService.upload(multipartFile);
    }

    @GetMapping("/data/images/car/{filename}")
    public ResponseEntity<Resource> serveFiles(@PathVariable String filename) throws IOException{
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Path.of(filename)))
                .body(imageService.serveFiles(filename));
    }
}

