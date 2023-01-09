package com.praca.komis.project.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BuyDTO {
    @NotBlank
    @NotNull
    private LocalDateTime meetDate;
    private String description;
    private BigDecimal totalBuyCost;
    private Long user;
    @NotNull
    private Long car;

}
