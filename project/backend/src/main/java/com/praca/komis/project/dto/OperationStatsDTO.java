package com.praca.komis.project.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class OperationStatsDTO {
    private List<Integer> labels;
    private List<Long> amount;
    private List<BigDecimal> value;
}
