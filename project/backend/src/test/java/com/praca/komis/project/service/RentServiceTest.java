package com.praca.komis.project.service;

import com.praca.komis.project.dto.RentDTO;
import com.praca.komis.project.model.*;
import com.praca.komis.project.repository.BuyRepository;
import com.praca.komis.project.repository.CarRepository;
import com.praca.komis.project.repository.RentRepository;
import com.praca.komis.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentServiceTest {

    @InjectMocks
    private RentService rentService;

    @Mock
    private RentRepository rentRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BuyRepository buyRepository;

    @Mock
    private EmailService emailService;

    @Test
    void shouldRentCar() {
        //given
        RentDTO rentDTO = createRentDTO();
        when(userRepository.findById(any())).thenReturn(createUser());
        when(carRepository.findById(any())).thenReturn(createCar());
        when(rentRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
//        when(rentRepository.save(any())).thenReturn(createRent());
        Long userId = Math.abs(new Random().nextLong());

        //when
        var rent = rentService.saveRent(rentDTO, userId);

        //then
        assertThat(rent).isNotNull();
        assertThat(rent.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldGetRentById() {
        Rent rent = createRent();
        when(rentRepository.findById(rent.getId())).thenReturn(Optional.of(rent));

        Rent actualRent = rentService.getOneRent(rent.getId()).getBody();
        verify(rentRepository, times(1)).findById(rent.getId());

        assertAll(() -> {
            assertThat(actualRent).isNotNull();
            assertThat(actualRent).usingRecursiveComparison().isEqualTo(rent);
        });
    }

    @Test
    void shouldFailGetRentById() {
        Rent rent = createRent();
        when(rentRepository.findById(rent.getId())).thenReturn(Optional.empty());

        var actualRent = rentService.getOneRent(rent.getId());

        assertThat(actualRent.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldGetAllRentals() {
        List<Rent> expectedRentals = List.of(createRent(),
                Rent.builder().id(2L).build(),
                Rent.builder().id(3L).build());

        when(rentRepository.findAll()).thenReturn(expectedRentals);
        List<Rent> actualRentals = rentService.getAllRent().getBody();
        verify(rentRepository, times(1)).findAll();

        assertAll(() -> {
            assertThat(actualRentals).isNotNull();
            assertThat(actualRentals.size()).isEqualTo(expectedRentals.size());
        });
    }

    @Test
    void shouldGetAllRentals_EmptyList() {
        when(rentRepository.findAll()).thenReturn(new ArrayList<>());

        List<Rent> actualRentals = rentService.getAllRent().getBody();

        assertThat(actualRentals).isEmpty();
    }

    @Test
    void shouldDeleteRent() {
        Rent rent = createRent();

        given(rentRepository.existsById(any())).willReturn(true);
        var actualRent = rentService.deleteRent(rent.getId());

        assertAll(() -> {
            assertThat(actualRent).isNotNull();
            assertThat(actualRent.getStatusCode()).isEqualTo(HttpStatus.OK);
        });
    }

    @Test
    void shouldFailDeleteRent() {
        Rent rent = createRent();

        given(rentRepository.existsById(any())).willReturn(false);
        var actualRent = rentService.deleteRent(rent.getId());

        assertAll(() -> {
            assertThat(actualRent).isNotNull();
            assertThat(actualRent.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        });
    }


    private Optional<Car> createCar() {
        return Optional.of(Car.builder()
                .id(2L)
                .brand("brand")
                .model("model")
                .registration("ABCDEFGH")
                .availableStatus(true)
                .buyPrice(BigDecimal.valueOf(123000))
                .rentBasePrice(BigDecimal.valueOf(123))
                .carDetail(CarDetail.builder()
                        .id(2L)
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
                .build());
    }

    private Optional<User> createUser() {
        return Optional.of(User.builder()
                .id(2L)
                .password("test")
                .name("test")
                .surname("test")
                .phone("123456789")
                .numberOfRent(0)
                .numberOfBuy(0)
                .build());
    }

    private RentDTO createRentDTO() {
        return RentDTO.builder()
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .description("description")
                .user(2L)
                .car(2L)
                .payment(PaymentType.CASH)
                .redirectUrl("")
                .build();
    }

    private Rent createRent() {
        return Rent.builder()
                .id(1L)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .rentDate(LocalDateTime.now())
                .description("description")
                .totalCost(BigDecimal.valueOf(1000))
                .user(createUser().orElseThrow())
                .car(createCar().orElseThrow())
                .payment(PaymentType.CASH)
                .build();
    }

}
