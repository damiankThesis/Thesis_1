package com.praca.komis.project.service;

import com.praca.komis.project.dto.RentDTO;
import com.praca.komis.project.model.*;
import com.praca.komis.project.repository.BuyRepository;
import com.praca.komis.project.repository.CarRepository;
import com.praca.komis.project.repository.RentRepository;
import com.praca.komis.project.repository.UserRepository;
import com.praca.komis.project.service.payment.PaymentPrzelewy24Service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentService {

    private static final Logger logger = LoggerFactory.getLogger(RentService.class);

    @Autowired
    private final RentRepository rentRepository;

    @Autowired
    private final CarRepository carRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BuyRepository buyRepository;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final PaymentPrzelewy24Service paymentPrzelewy24Service;

    public ResponseEntity<List<Rent>> getAllRent() {
        return new ResponseEntity<>(rentRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<List<Rent>> getRentByUser(Long id) {
        return new ResponseEntity<>(rentRepository.findByUserId(id), HttpStatus.OK);
    }

    public ResponseEntity<List<Rent>> getRentByCar(Long id) {
        return new ResponseEntity<>(rentRepository.findByCar_Id(id), HttpStatus.OK);
    }

    public ResponseEntity<Rent> getOneRent(Long id) {
        return rentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> saveRent(RentDTO rentDTO, Long userId) {

        User user = userRepository.findById(userId).orElseThrow();
        Car car = carRepository.findById(rentDTO.getCar())
                .orElseThrow(() -> new EntityNotFoundException("User with this id doesn't exist"));

        if(validateRentDate(rentDTO, car)) {
            Rent rent = mapToRent(rentDTO, car.getRentBasePrice());
            rent.setCar(car);
            user.setNumberOfRent(user.getNumberOfRent() + 1);
            rent.setUser(user);

            Rent r = rentRepository.save(rent);
            emailService.send(user.getUsername(), "Rental confirmation", createMessage(car, user, rentDTO));
            String tokenUrl = initPayment(rent);
            return new ResponseEntity<>(createResponse(r, tokenUrl), HttpStatus.CREATED);
        }

        return ResponseEntity.noContent().build();
    }

    private boolean validateRentDate(RentDTO rentDTO, Car car) {
        List<Rent> rentals = rentRepository.findByCar_Id(car.getId());
        List<Buy> buys = buyRepository.findByCar_Id(car.getId());

        boolean check = rentals.stream()
                    .map(rent -> ( (rentDTO.getStartDate().plusHours(2).isAfter(rent.getEndDate())
                        || rentDTO.getEndDate().plusHours(2).isBefore(rent.getStartDate()))) )
                    .collect(Collectors.toList()).stream().allMatch(aBoolean -> aBoolean.equals(true))
                    && ( !buys.stream()
                        .allMatch(buy -> buy.getMeetDate().isAfter(rentDTO.getStartDate())
                        && buy.getMeetDate().isBefore(rentDTO.getEndDate())) || buys.isEmpty());

        return (rentals.isEmpty() || check)
                && rentDTO.getStartDate().plusHours(2).isAfter(LocalDateTime.now())
                && rentDTO.getStartDate().isBefore(rentDTO.getEndDate()
        );

    }

    private RentDTO createResponse(Rent r, String tokenUrl) {
        return RentDTO.builder()
                .startDate(r.getStartDate())
                .endDate(r.getEndDate())
                .description(r.getDescription())
                .redirectUrl(tokenUrl)
                .build();
    }

    private String initPayment(Rent rent) {
        if(rent.getPayment() == PaymentType.PRZELEWY24_ONLINE)
            return paymentPrzelewy24Service.initPayment(rent);
        return null;
    }

    private String createMessage(Car car, User user, RentDTO rentDTO) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("Hello, %s! \n\nYou rented a car: %s %s\nRental period: %s - %s\ntotal amount: %szł",
                user.getName(), car.getBrand(), car.getModel(),
                LocalDate.parse(rentDTO.getStartDate().plusDays(1).format(dateFormatter), dateFormatter),
                LocalDate.parse(rentDTO.getEndDate().plusDays(1).format(dateFormatter), dateFormatter),
                calculateTotalRentPrice(rentDTO, car.getRentBasePrice()));
    }

    Rent mapToRent(RentDTO rentDTO, BigDecimal cost) {
        return Rent.builder()
                .startDate(rentDTO.getStartDate().plusHours(1))
                .endDate(rentDTO.getEndDate().plusHours(1))
                .rentDate(LocalDateTime.now())
                .description(rentDTO.getDescription())
                .totalCost(calculateTotalRentPrice(rentDTO, cost))
                .payment(rentDTO.getPayment())
                .build();
    }

    private BigDecimal calculateTotalRentPrice(RentDTO rentDTO, BigDecimal cost) {
       return cost.multiply(BigDecimal.valueOf(Duration.between(rentDTO.getStartDate(), rentDTO.getEndDate()).toDays())).abs();
    }

    public ResponseEntity<?> deleteRent(Long id) {
        if(!rentRepository.existsById(id))
            return ResponseEntity.notFound().build();

        rentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<String[]>> getOneRentDate(Long id) {
        logger.info(String.valueOf(id));
        List<String[]> res = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        rentRepository.findByCar_Id(id).forEach(rent -> {
            if (rent.getEndDate().isAfter(LocalDateTime.now())) {
                res.add(new String[]{
                    rent.getStartDate().format(formatter),
                    rent.getEndDate().format(formatter)
                });
            }
        });
        return ResponseEntity.ok(res);
    }

}

