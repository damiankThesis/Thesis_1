package com.praca.komis.project.dto;

import com.praca.komis.project.model.PaymentType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class RentDTO {
    @NotBlank
    @NotNull
    private LocalDateTime startDate;
    @NotBlank
    @NotNull
    private LocalDateTime endDate;
    private String description;
    private Long user;
    @NotNull
    private Long car;

    @NotNull
    private PaymentType payment;
    private String redirectUrl;
}
