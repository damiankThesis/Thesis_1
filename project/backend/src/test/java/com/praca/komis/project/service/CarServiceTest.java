package com.praca.komis.project.service;

import com.praca.komis.project.dto.CarDTO;
import com.praca.komis.project.model.Car;
import com.praca.komis.project.model.CarDetail;
import com.praca.komis.project.repository.CarDetailRepository;
import com.praca.komis.project.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @InjectMocks
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarDetailRepository carDetailRepository;

    @Test
    void shouldGetCarById() {
        Car car = createCar();
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        Car actualCar = carService.getCarById(car.getId()).getBody();
        verify(carRepository, times(1)).findById(car.getId());

        assertAll(() -> {
            assertThat(actualCar).isNotNull();
            assertThat(actualCar).usingRecursiveComparison().isEqualTo(car);
        });
    }

    @Test
    void shouldFailGetCarById() {
        Car car = createCar();
        when(carRepository.findById(car.getId())).thenReturn(Optional.empty());

        var actualCar = carService.getCarById(car.getId());

        assertThat(actualCar.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldAddCar() {
        //given
        CarDTO carDTO = createCarDTO();

        //when
        var car = carService.saveCar(carDTO);

        //then
        assertAll( () -> {
            assertThat(car).isNotNull();
            assertThat(car.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        });
    }

    @Test
    void shouldGetAllCars() {
        //given
        List<Car> expectedCarsList = List.of(createCar(),
                Car.builder().id(2L).build(),
                Car.builder().id(3L).build());

        when(carRepository.findAll()).thenReturn(expectedCarsList);
        List<Car> actualCars = carService.getAllCars().getBody();
        verify(carRepository, times(1)).findAll();

        assertAll(() -> {
            assertThat(actualCars).isNotNull();
            assertThat(actualCars.size()).isEqualTo(expectedCarsList.size());
        });
    }

    @Test
    void shouldGetAllCars_EmptyList() {
        when(carRepository.findAll()).thenReturn(new ArrayList<>());

        List<Car> actualCars = carService.getAllCars().getBody();

        assertThat(actualCars).isEmpty();
    }

    @Test
    void shouldDeleteCar() {
        Car car = createCar();

        given(carRepository.existsById(any())).willReturn(true);
        var actualCar = carService.deleteCar(car.getId());

        assertAll(() -> {
            assertThat(actualCar).isNotNull();
            assertThat(actualCar.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        });
    }

    @Test
    void shouldFailDeleteCar() {
        Car car = createCar();

        given(carRepository.existsById(any())).willReturn(false);
        var actualCar = carService.deleteCar(car.getId());

        assertAll(() -> {
            assertThat(actualCar).isNotNull();
            assertThat(actualCar.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        });
    }

    @Test
    void shouldToggleCarStatus() {
        Car car = createCar();

        given(carRepository.existsById(any())).willReturn(true);
        var actualCar = carService.toggleCarStatus(car.getId());

        assertAll(() -> {
            assertThat(actualCar).isNotNull();
            assertThat(actualCar.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        });
    }

    @Test
    void shouldNoToggleCarStatus() {
        Car car = createCar();

        given(carRepository.existsById(any())).willReturn(false);
        var actualCar = carService.toggleCarStatus(car.getId());

        assertThat(actualCar.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Car createCar() {
        return Car.builder()
                .id(1L)
                .brand("brand")
                .model("model")
                .registration("registration")
                .availableStatus(true)
                .buyPrice(BigDecimal.valueOf(123000))
                .rentBasePrice(BigDecimal.valueOf(100))
                .image("image")
                .carDetail(CarDetail.builder()
                        .id(1L)
                        .type("type")
                        .productionYear(2000)
                        .fuel("fuel")
                        .power(200)
                        .capacity(2000)
                        .gearbox("gearbox")
                        .drive("drive")
                        .doors(5)
                        .seats(5)
                        .color("color")
                        .description("desc")
                        .build())
                .build();
    }

    private CarDTO createCarDTO() {
        return CarDTO.builder()
                .brand("brand")
                .model("model")
                .registration("registration")
                .availableStatus(true)
                .buyPrice(BigDecimal.valueOf(123000))
                .rentBasePrice(BigDecimal.valueOf(100))
                .image("image")
                .type("type")
                .productionYear(2000)
                .fuel("fuel")
                .power(200)
                .capacity(2000)
                .gearbox("gearbox")
                .drive("drive")
                .doors(5)
                .seats(5)
                .color("color")
                .description("desc")
                .build();
    }
}
