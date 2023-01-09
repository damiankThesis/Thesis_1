package com.praca.komis.project.service;

import com.praca.komis.project.dto.BuyDTO;
import com.praca.komis.project.model.Buy;
import com.praca.komis.project.model.Car;
import com.praca.komis.project.model.User;
import com.praca.komis.project.repository.BuyRepository;
import com.praca.komis.project.repository.CarRepository;
import com.praca.komis.project.repository.RentRepository;
import com.praca.komis.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BuyService {

    @Autowired
    private final BuyRepository buyRepository;

    @Autowired
    private final CarRepository carRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RentRepository rentRepository;

    @Autowired
    private final EmailService emailService;


    public ResponseEntity<List<Buy>> getAllBuy() {
        return new ResponseEntity<>(buyRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Buy> getOneBuy(Long id) {
        return buyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Buy>> getBuyByUser(Long id) {
        return new ResponseEntity<>(buyRepository.findByUserId(id), HttpStatus.OK);
    }

    public ResponseEntity<List<Buy>> getBuyByCar(Long id) {
        return new ResponseEntity<>(buyRepository.findByCar_Id(id), HttpStatus.OK);
    }

    public ResponseEntity<?> saveBuy(BuyDTO buyDTO, Long userId) {

        Car car = carRepository.findById(buyDTO.getCar())
                .orElseThrow(() -> new EntityNotFoundException("User with this id doesn't exist"));
        User user = userRepository.findById(userId).orElseThrow();

        if(checkDate(buyDTO, car)) {
            Buy buy = mapToBuy(buyDTO); buy.setCar(car);
            user.setNumberOfBuy(user.getNumberOfBuy() + 1);
            buy.setUser(user);
            car.setAvailableStatus(false);

            emailService.send(user.getUsername(), "Purchase confirmation",
                    createMessage(car, user, buyDTO.getMeetDate(), buyDTO.getTotalBuyCost()));
            return new ResponseEntity<>(buyRepository.save(buy), HttpStatus.CREATED);
        }

        return ResponseEntity.noContent().build();
    }

    private boolean checkDate(BuyDTO buyDTO, Car car) {
        return rentRepository.findByCar_Id(car.getId())
                .stream()
                .allMatch(rent -> rent.getEndDate().isBefore(buyDTO.getMeetDate()))
            && buyRepository.findByCar_Id(car.getId())
                .stream()
                .allMatch(buy -> buy.getMeetDate().isEqual(buyDTO.getMeetDate()));
    }

    private String createMessage(Car car, User user, LocalDateTime date, BigDecimal total) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("Hello, %s! \n\nYou bought a car: %s %s\nTotal: %s zł\nDate: %s",
                user.getName(), car.getBrand(), car.getModel(), total, LocalDate.parse(date.format(dateFormatter), dateFormatter));
    }

    public ResponseEntity<?> deleteBuy(Long id) {
        if (!buyRepository.existsById(id))
            return ResponseEntity.notFound().build();

        Car car = buyRepository.findById(id).orElseThrow().getCar();
        car.setAvailableStatus(true);

        carRepository.save(car);
        buyRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public Buy mapToBuy(BuyDTO buyDTO) {
        return Buy.builder()
                .meetDate(buyDTO.getMeetDate().plusHours(2))
                .reqDate(LocalDateTime.now())
                .description(buyDTO.getDescription())
                .totalBuyCost(buyDTO.getTotalBuyCost())
                .build();
    }

}
