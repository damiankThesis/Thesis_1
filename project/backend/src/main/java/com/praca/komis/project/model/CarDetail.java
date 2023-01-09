package com.praca.komis.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "car_details")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarDetail {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private Integer productionYear;
    private String fuel;
    private Integer power;
    private Integer capacity;
    private String gearbox;
    private String drive;
    private Integer doors;
    private Integer seats;
    private String color;
    private String description;

    @JsonIgnore
    @OneToOne(mappedBy = "carDetail")
    private Car car;
}
