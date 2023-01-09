package com.praca.komis.project.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cars")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String model;
    private String registration;
    private Boolean availableStatus;
    private BigDecimal buyPrice;
    private BigDecimal rentBasePrice;
    private String image;
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "carId")
    private CarDetail carDetail;
}
