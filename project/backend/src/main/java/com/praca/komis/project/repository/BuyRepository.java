package com.praca.komis.project.repository;

import com.praca.komis.project.model.Buy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BuyRepository extends JpaRepository<Buy, Long> {
    List<Buy> findByUserId(Long id);
    List<Buy> findByCar_Id(Long id);

    List<Buy> findAllByMeetDateIsBetween(LocalDateTime from, LocalDateTime to);

}
