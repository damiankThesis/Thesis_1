package com.praca.komis.project.repository;

import com.praca.komis.project.model.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {
    List<Rent> findByUserId(Long id);

    List<Rent> findByCar_Id(Long id);

    List<Rent> findAllByStartDateIsBetween(LocalDateTime from, LocalDateTime to);

    @Query("SELECT r from Rent r where r.car=:id")
    List<LocalDateTime> findAllDateByCar_Id(Long id);
}
