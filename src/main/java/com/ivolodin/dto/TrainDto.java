package com.ivolodin.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TrainDto{

    @JsonView(View.Public.class)
    @NotBlank
    private String trainName;

    @JsonView(View.Public.class)
    @Positive
    private Integer seatsNumber;

    @JsonView(View.Public.class)
    @NotBlank
    @Size(min = 3, max = 45)
    private String fromStation;

    @JsonView(View.Public.class)
    @NotBlank
    @Size(min = 3, max = 45)
    private String toStation;

    @JsonView(View.Public.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departure;

    @JsonView(View.Public.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrival;

    @JsonView(View.Public.class)
    private List<TrainEdgeDto> path;

}
