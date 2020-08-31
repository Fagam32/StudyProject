package com.ivolodin.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
public class TrainEdgeDto {
    private String trainName;
    private Integer order;
    private String stationName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departure;

    @PositiveOrZero
    private Integer standing;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrival;
}
