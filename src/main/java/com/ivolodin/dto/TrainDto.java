package com.ivolodin.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TrainDto {
    private String trainName;
    private Integer seatsNumber;
    private String fromStation;
    private String toStation;
    private LocalDateTime departure;
    private LocalDateTime arrival;
    private List<TrainEdgeDto> path;
}
