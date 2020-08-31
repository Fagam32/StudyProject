package com.ivolodin.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TrainDto {

    @NotBlank
    private String trainName;

    @Positive
    private Integer seatsNumber;

    @NotBlank
    @Size(min = 3, max = 45)
    private String fromStation;

    @NotBlank
    @Size(min = 3, max = 45)
    private String toStation;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departure;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrival;

    private List<TrainEdgeDto> path;

    private List<TicketDto> tickets;

}
