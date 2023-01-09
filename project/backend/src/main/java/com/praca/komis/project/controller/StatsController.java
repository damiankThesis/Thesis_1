package com.praca.komis.project.controller;

import com.praca.komis.project.dto.OperationStatsDTO;
import com.praca.komis.project.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatsController {

    @Autowired
    private final StatsService statsService;

    @GetMapping("/rent")
    public OperationStatsDTO getRentStats() {
        return statsService.getRentStatistics();
    }

    @GetMapping("/buy")
    public OperationStatsDTO getBuyStats() {
        return statsService.getBuyStatistics();
    }
}
