package com.ivolodin.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainEdgeDto {

    @JsonView(View.Public.class)
    private String trainName;

    @JsonView(View.Public.class)
    private Integer order;

    @JsonView(View.Public.class)
    private String stationName;

    @JsonView(View.Public.class)
    private Integer seatsLeft;

    @JsonView(View.Public.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrival;

    @JsonView(View.Public.class)
    @PositiveOrZero
    private Integer standing;

    @JsonView(View.Public.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departure;

}
