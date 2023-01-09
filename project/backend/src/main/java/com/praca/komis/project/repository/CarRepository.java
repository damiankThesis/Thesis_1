package com.praca.komis.project.repository;

import com.praca.komis.project.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT c FROM Car c WHERE c.availableStatus = true")
    List<Car> findAvailableCars();

    @Query("SELECT c FROM Car c WHERE c.availableStatus = true")
    Page<Car> findAvailableCars(Pageable pageable);

}
