package com.praca.komis.project.service;

import com.praca.komis.project.dto.CarDTO;
import com.praca.komis.project.model.Car;
import com.praca.komis.project.model.CarDetail;
import com.praca.komis.project.repository.CarDetailRepository;
import com.praca.komis.project.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private static final Logger logger = LoggerFactory.getLogger(CarService.class);

    @Autowired
    private final CarRepository carRepository;

    @Autowired
    private final CarDetailRepository carDetailRepository;

    public ResponseEntity<List<Car>> getAllCars() {
        return new ResponseEntity<>(carRepository.findAll(), HttpStatus.OK);
    }

    public Page<?> getAllCars(Pageable pageable) {
        return carRepository.findAll(pageable);
//        return new ResponseEntity<>(carRepository.findAll(), HttpStatus.OK);
    }

    public Page<?> getAvailableCar(Pageable pageable) {
        return carRepository.findAvailableCars(pageable);
    }

    public ResponseEntity<Car> getCarById(long id) {
        return carRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<CarDetail>> getCarDetails() {
        return new ResponseEntity<>(carDetailRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Car> saveCar(CarDTO carDto) {
        Car car = mapToCar(carDto);
        var result = carRepository.save(car);
        return ResponseEntity.created(URI.create("/"+car.getId())).body(result);
    }

    Car mapToCar(CarDTO carDto) {
        CarDetail carDetail = CarDetail.builder()
                .type(carDto.getType())
                .productionYear(carDto.getProductionYear())
                .fuel(carDto.getFuel())
                .power(carDto.getPower())
                .capacity(carDto.getCapacity())
                .gearbox(carDto.getGearbox())
                .drive(carDto.getDrive())
                .doors(carDto.getDoors())
                .seats(carDto.getSeats())
                .color(carDto.getColor())
                .description(carDto.getDescription())
                .build();

        carDetailRepository.save(carDetail);

        return Car.builder()
                .brand(carDto.getBrand())
                .model(carDto.getModel())
                .registration(carDto.getRegistration().toUpperCase())
                .availableStatus(carDto.getAvailableStatus())
                .buyPrice(carDto.getBuyPrice())
                .rentBasePrice(carDto.getRentBasePrice())
                .carDetail(carDetail)
                .image(carDto.getImage())
                .build();
    }

    public Car mapToCarUpdate(CarDTO carDto, Long id) {

        CarDetail carDetail = CarDetail.builder()
                .id(id)
                .type(carDto.getType())
                .productionYear(carDto.getProductionYear())
                .fuel(carDto.getFuel())
                .power(carDto.getPower())
                .capacity(carDto.getCapacity())
                .gearbox(carDto.getGearbox())
                .drive(carDto.getDrive())
                .doors(carDto.getDoors())
                .seats(carDto.getSeats())
                .color(carDto.getColor())
                .description(carDto.getDescription())
                .build();

        carDetailRepository.save(carDetail);

        return Car.builder()
                .brand(carDto.getBrand())
                .model(carDto.getModel())
                .registration(carDto.getRegistration().toUpperCase())
                .availableStatus(carDto.getAvailableStatus())
                .buyPrice(carDto.getBuyPrice())
                .rentBasePrice(carDto.getRentBasePrice())
                .carDetail(carDetail)
                .image(carDto.getImage())
                .build();
    }

    public ResponseEntity<Car> updateCar(long id, CarDTO carDto) {
        var car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car with this id doesn't exist"));

        car = mapToCarUpdate(carDto, car.getCarDetail().getId());
        car.setId(id);

        return new ResponseEntity<>(carRepository.save(car), HttpStatus.OK);
    }

    public Car setCarDetails(Long id, CarDetail carDetail) {
        var car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car with this id doesn't exist"));

        if(car.getCarDetail() != null)
            carDetailRepository.delete(car.getCarDetail());

        carDetailRepository.save(carDetail);
        car.setCarDetail(carDetail);
        return carRepository.save(car);
    }

    public ResponseEntity<?> deleteCar(Long id) {
        if (!carRepository.existsById(id))
            return ResponseEntity.notFound().build();

        carRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> toggleCarStatus(Long id) {
        if(!carRepository.existsById(id)) {
            logger.info("Not Exists!");
            return ResponseEntity.notFound().build();
        }

        carRepository.findById(id)
                .ifPresent(car -> car.setAvailableStatus(!car.getAvailableStatus()));

        return ResponseEntity.noContent().build();
    }
}
