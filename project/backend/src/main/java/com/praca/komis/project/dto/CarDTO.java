package com.praca.komis.project.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@Builder
public class CarDTO {

    @NotBlank
    @Length(min = 3)
    private String brand;
    @NotBlank
    @Length(min = 3)
    private String model;
    @NotBlank
    @Length(min = 4, max = 8)
    private String registration;
    private Boolean availableStatus;

    @PositiveOrZero
    private BigDecimal buyPrice;

    @PositiveOrZero
    private BigDecimal rentBasePrice;

    private String image;
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

}
