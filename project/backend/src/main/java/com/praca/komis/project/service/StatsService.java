package com.praca.komis.project.service;

import com.praca.komis.project.dto.OperationStatsDTO;
import com.praca.komis.project.model.Buy;
import com.praca.komis.project.model.Rent;
import com.praca.komis.project.repository.BuyRepository;
import com.praca.komis.project.repository.RentRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final RentRepository rentRepository;
    private final BuyRepository buyRepository;

    public OperationStatsDTO getRentStatistics() {
        //od pierwszego dnia biezacego miesiaca
        LocalDateTime from = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = LocalDateTime.now();

        List<Rent> rents = rentRepository.findAllByStartDateIsBetween(from, to);

        TreeMap<Integer, StatsValue> res = new TreeMap<>();
        for(int i = from.getDayOfMonth(); i <= to.getDayOfMonth(); i++)
            res.put(i, aggValuesRent(i, rents));


        return OperationStatsDTO.builder()
                .labels(new ArrayList<>(res.keySet()))
                .amount(res.values().stream()
                        .map(o -> o.amount)
                        .collect(Collectors.toList()))
                .value(res.values().stream()
                        .map(o -> o.total)
                        .collect(Collectors.toList()))
                .build();
    }

    private StatsValue aggValuesRent(int i, List<Rent> rents) {
        BigDecimal total = BigDecimal.ZERO;
        Long amount = 0L;

        for(var rent: rents) {
            if(i==rent.getStartDate().getDayOfMonth()) {
                total = total.add(rent.getTotalCost());
                amount++;
            }
        }
        return new StatsValue(total, amount);
    }

    public OperationStatsDTO getBuyStatistics() {
        LocalDateTime from = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = LocalDateTime.now();

        List<Buy> buys = buyRepository.findAllByMeetDateIsBetween(from, to);

        TreeMap<Integer, StatsValue> res = new TreeMap<>();
        for(int i = from.getDayOfMonth(); i <= to.getDayOfMonth(); i++)
            res.put(i, aggValuesBuy(i, buys));

        return OperationStatsDTO.builder()
                .labels(new ArrayList<>(res.keySet()))
                .amount(res.values().stream()
                        .map(o -> o.amount)
                        .collect(Collectors.toList()))
                .value(res.values().stream()
                        .map(o -> o.total)
                        .collect(Collectors.toList()))
                .build();
    }

    private StatsValue aggValuesBuy(int i, List<Buy> rents) {
        BigDecimal total = BigDecimal.ZERO;
        Long amount = 0L;

        for(var buy: rents) {
            if(i==buy.getMeetDate().getDayOfMonth()) {
                total = total.add(buy.getTotalBuyCost());
                amount++;
            }
        }
        return new StatsValue(total, amount);
    }

    //response format
    @AllArgsConstructor
    @Getter
    private static class StatsValue {
        private BigDecimal total;
        private Long amount;
    }
}
