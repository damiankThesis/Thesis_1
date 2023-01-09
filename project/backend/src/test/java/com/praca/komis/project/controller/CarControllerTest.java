package com.praca.komis.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.praca.komis.project.model.Car;
import com.praca.komis.project.model.CarDetail;
import com.praca.komis.project.repository.CarRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarRepository carRepository;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @SneakyThrows
    void CarController_findAllCars_ReturnOK()  {
        given(carRepository.findAll()).willReturn(List.of(Car.builder().build()));
        mockMvc.perform(get("/api/v1/cars").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void CarController_findAllCarsEmptyList_ReturnOK()  {
        given(carRepository.findAll()).willReturn(new ArrayList<>());
        mockMvc.perform(get("/api/v1/cars").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void CarController_findCarById_ReturnOK() throws Exception {
        Long id = Math.abs(new Random().nextLong());
        Car car = initCar();
        given(carRepository.findById(any())).willReturn(Optional.of(car));
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/api/v1/cars/{id}", id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(car.getId().intValue())))
                .andExpect(jsonPath("$.brand", is(car.getBrand())))
                .andExpect(jsonPath("$.model", is(car.getModel())))
                .andExpect(jsonPath("$.registration", is(car.getRegistration())))
                .andExpect(jsonPath("$.buyPrice", is(car.getBuyPrice().intValue())))
                .andExpect(jsonPath("$.rentBasePrice", is(car.getRentBasePrice().intValue())))
                .andExpect(jsonPath("$.availableStatus", is(car.getAvailableStatus())))
                .andExpect(jsonPath("$.carDetail.type", is(car.getCarDetail().getType())))
                .andExpect(jsonPath("$.carDetail.productionYear", is(car.getCarDetail().getProductionYear())))
                .andExpect(jsonPath("$.carDetail.fuel", is(car.getCarDetail().getFuel())))
                .andExpect(jsonPath("$.carDetail.power", is(car.getCarDetail().getPower())))
                .andExpect(jsonPath("$.carDetail.capacity", is(car.getCarDetail().getCapacity())))
                .andExpect(jsonPath("$.carDetail.gearbox", is(car.getCarDetail().getGearbox())))
                .andExpect(jsonPath("$.carDetail.drive", is(car.getCarDetail().getDrive())))
                .andExpect(jsonPath("$.carDetail.doors", is(car.getCarDetail().getDoors())))
                .andExpect(jsonPath("$.carDetail.seats", is(car.getCarDetail().getSeats())))
                .andExpect(jsonPath("$.carDetail.color", is(car.getCarDetail().getColor())))
                .andExpect(jsonPath("$.carDetail.description", is(car.getCarDetail().getDescription())));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"ADMIN"})
    @SneakyThrows
    void CarController_addCar_ReturnCreated() {
        given(carRepository.save(any())).willReturn(Car.builder().build());
        mockMvc.perform(post("/api/v1/cars")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(initCar())))
                    .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"ADMIN"})
    @SneakyThrows
    void CarController_noAddCar_ReturnBadRequest() {
        given(carRepository.save(any())).willReturn(Car.builder().build());
        mockMvc.perform(post("/api/v1/cars")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(null)))
                    .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"ADMIN"})
    @SneakyThrows
    void CarController_deleteExistingCar_ReturnNoContent()  {
        given(carRepository.existsById(any())).willReturn(true);
        mockMvc.perform(delete("/api/v1/cars/{id}", Math.abs(new Random().nextInt()))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"ADMIN"})
    @SneakyThrows
    void CarController_deleteNoExistingCar_ReturnNotFound()  {
        given(carRepository.existsById(any())).willReturn(false);
        mockMvc.perform(delete("/api/v1/cars/{id}", Math.abs(new Random().nextInt()))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound());
    }

    private Car initCar() {
       return Car.builder()
                .id(1L)
                .brand("brand")
                .model("model")
                .registration("ABCDE123")
                .buyPrice(BigDecimal.valueOf(123000))
                .rentBasePrice(BigDecimal.valueOf(123))
                .availableStatus(true)
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
                        .description("des")
                        .build()
                )
                .image("image")
                .build();
    }

}
