package com.praca.komis.project.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "buys")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Buy implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime meetDate;
    private LocalDateTime reqDate;
    private String description;
    private BigDecimal totalBuyCost;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private Car car;
}
