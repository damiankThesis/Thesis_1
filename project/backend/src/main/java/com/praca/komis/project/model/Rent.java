package com.praca.komis.project.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rents")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rent implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime rentDate;
    private String description;
    private BigDecimal totalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Car car;

    private PaymentType payment;
}
