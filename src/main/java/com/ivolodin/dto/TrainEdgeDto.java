package com.ivolodin.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainEdgeDto {
    private String trainName;
    private String fromStation;
    private String toStation;
    private LocalDateTime departure;
    private Integer standing;
    private LocalDateTime arrival;
}
